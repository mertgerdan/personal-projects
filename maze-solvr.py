import pygame as pg
import numpy as np
import random
import heapq




# Implements A* for speedy pathfinding.
# Sebastian Lague's series on A* Pathfinding were amazing.
# Acknowledgements for my A* algorithm implementation 
# go to Sebastian Lague's youtube series on A* Pathfinding,
# and also Amit Patel's brilliant description of general pathfinding strategies on his blog.
# http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html
# Algorithm implementation is my work, those acknowledgements helped me understand and apply the algorithm.

# 4 of the 8 mazes made were done so by this open source maze generator -> I slightly modified it to make
# numpy array convertion possible, here is the link: https://github.com/DivXor/epamRD_maze/blob/master/src/main/java/com/epam/trash/MyMaze.java
# Other 4 are my manual work, ranging in 3 different sizes. maze_1, maze_6, maze_7, maze_8 are my handmade creation.
# Picked out a small template, copied and modified it for maze 6 and 8 to make those shapes. Others are a single template made from scratch.

####################################################################
# A* Pathfinding with Heap Queue Visualized
#
# 0 = empty
# 1 = wall
# maze can't traverse diagonally, so 4 child nodes (0,-1) (1,0) (0,1) (-1,0)
# Priority queue implementation for faster priority finding at O(1)
# F = G + H
# Lowest heuristic gives shortest path potential
####################################################################


def get_H(pos1, pos2):
    return abs(pos1[0] - pos2[0]) + abs(pos1[1] - pos2[1])

def a_star(start, end, input_array):

    # Since F = G + H, initializing this way instead of calculating G,H
    # each time is a bit more neat
    gscore = {start:0} # starting node has g value of 0
    fscore = {start:get_H(start,end)} 
    visited_list = {} # squares we can consider to go
    closed_set = set() # squares that dont need going
    heap_q = []

    #push queue
    heapq.heappush(heap_q, (fscore[start], start))

    children = [(0,-1),(1,0),(0,1),(-1,0)]

    while heap_q:

        current_node = heapq.heappop(heap_q)[1] # get current square node from queue

        if current_node == end: #if we arrive to our end pos, return path
            final_path = []
            while current_node in visited_list:
                final_path.append(current_node)
                current_node = visited_list[current_node] #need to reverse the list to find the path from start
            return final_path[::-1]

        #if explored square node is not the end node, add it to the closed list
        closed_set.add(current_node)

        for i,j in children:
            child_node = current_node[0] + i, current_node[1] + j

            #gets the current G + add the H value to it
            get_GH = gscore[current_node] + get_H(current_node, child_node)
            if 0 <= child_node[0] < input_array.shape[0]: #check for arrayOutOfBounds
                if 0 <= child_node[1] < input_array.shape[1]:                
                    if input_array[child_node[0]][child_node[1]] == 1: #if wall, skip
                        continue
                else:
                    continue
            else:
                continue

            # skip if G+H is greater than the G score of the child square node.
            if get_GH >= gscore.get(child_node, 0):
                if child_node in closed_set: # I forgot this and spent an hour trying to figure out my problem hehe.
                    continue

            # otherwise update scores and push queue
            if get_GH < gscore.get(child_node, 0) or child_node not in [i[1]for i in heap_q]:
                visited_list[child_node] = current_node
                gscore[child_node] = get_GH
                fscore[child_node] = get_GH + get_H(child_node, end)
                heapq.heappush(heap_q, (fscore[child_node], child_node))

    # returns empty path if found no possible path from start to end
    return []



##########################
#
# Visualize Array
#
##########################

def get_random_maze(): # loads in random maze from directory
    return "maze_{}.txt".format(random.randrange(8) + 1)

maze = np.loadtxt(get_random_maze())
# how many mazes the player solves
maze_count = 0

# create a 3D array with the shape of the previous array
cells = np.ndarray((maze.shape[0], maze.shape[1], 3))

# color dictionary:
#0 = empty = white, 1 = wall = black, 2 = red = path, 3 = blue = player.
color_dict = {
        0: [255, 255, 255],#white
        1: [0, 0, 0], #black
        2: [255, 0, 0], #red
        3: [253, 224, 38] #blue
        }

# assign colors to values
for i in range(maze.shape[0]):
    for j in range(maze.shape[1]):
    	if maze[i][j] == 0:
    		cells[i][j] = color_dict[0]
    	else:
    		cells[i][j] = color_dict[1]

# our player cell
cells[0][0] = color_dict[3]
         
# set the size of the screen as multiples of the array
cellsize = 17 # largest desirable size to run on 1366x768 screen.
WIDTH = cells.shape[0] * cellsize
HEIGHT = cells.shape[1] * cellsize



##########################
#
# Pygame Implementation
#
##########################

# initialize pygame
pg.init()
clock = pg.time.Clock()
screen = pg.display.set_mode((WIDTH, HEIGHT))
pg.display.set_caption("Maze Solver")

#create a surface with the size as the array
surf = pg.Surface((cells.shape[0], cells.shape[1]))
# draw array onto the surface
pg.surfarray.blit_array(surf, cells)
# transform the surface to screen size
surf = pg.transform.scale(surf, (WIDTH, HEIGHT))
counter = 0 #counter for how many times mouse button is clicked (for pathfinding)

# Converts a click's x and y pixel positions to corresponding squares.
def click_to_coord(pos):
    cell_x = int(pos[0] / cellsize)
    cell_y = int(pos[1] / cellsize)

    if maze[cell_x][cell_y] == 1: # wall check
        return ((0,0))
    
    else:
        return ((cell_x,cell_y))

# Cleans maze and removes the path
def clean_maze():
    for i in range(maze.shape[0]):
        for j in range(maze.shape[1]):
            if not maze[i][j] == 1:
                cells[i][j] = color_dict[0]

# Check if position to move is legal
def checkAvailability(pos):
    if pos[0] < 0 or pos[0] >= maze.shape[0] or pos[1] < 0 or pos[1] >= maze.shape[1]:
        return False
    elif maze[pos[0]][pos[1]] == 1:
        return False
    else:
        return True

# Move the player
def player_move(key, pos):
    if key == pg.K_UP:
        if checkAvailability((pos[0],pos[1]-1)):
            return (pos[0],pos[1]-1)
    if key == pg.K_DOWN:
        if checkAvailability((pos[0],pos[1]+1)):
            return (pos[0],pos[1]+1)
    if key == pg.K_RIGHT:
        if checkAvailability((pos[0]+1,pos[1])):
            return (pos[0]+1,pos[1])
    if key == pg.K_LEFT:
        if checkAvailability((pos[0]-1,pos[1])):
            return (pos[0]-1,pos[1])
    return pos

# simply returns how many mazes you have completed
def get_maze_completed(maze_count):
    return maze_count



########################
#
# Introduction Messages
#
########################

welcome_msg = "Welcome to Maze Solver!"
welcome_msg2 = "Press enter to see your maze, move with arrow buttons."
welcome_msg3 = "For solution, click two squares: One for the starting position, and one for the end. Enjoy!"
FONT_BIG = pg.font.Font('freesansbold.ttf', 32) 
FONT_SMALL = pg.font.Font('freesansbold.ttf', 16)
welcome_text = FONT_BIG.render(welcome_msg, True, (255,255,255), (0,0,128))
welcome_text2 = FONT_SMALL.render(welcome_msg2, True, (255,255,255), (0,0,128))
welcome_text3 = FONT_SMALL.render(welcome_msg3, True, (255,255,255), (0,0,128))
welcomeTextRect = welcome_text.get_rect()
welcomeTextRect2 = welcome_text2.get_rect()
welcomeTextRect3 = welcome_text3.get_rect()  
welcomeTextRect.center = ((320 // 2) + 200, (320 // 2) - 50)
welcomeTextRect2.center = ((320 // 2) + 200, 320 // 2)
welcomeTextRect3.center = ((320 // 2) + 200, (320 // 2) + 20)


# Called when a player reaches the end of the maze
def show_win_credits(win_credits):

    # End credits
    global maze_count
    maze_count += 1
    screen = pg.display.set_mode((720, 320))
    screen.fill((20,5,50))
    win_msg = "You won! You have done {} mazes so far.".format(get_maze_completed(maze_count))
    win_msg2 = "To play another maze, press G."
    win_text = FONT_BIG.render(win_msg, True, (255,255,255), (0,0,128))
    win_text2 = FONT_SMALL.render(win_msg2, True, (255,255,255), (0,0,128))
    winTextRect = win_text.get_rect()
    winTextRect2 = win_text2.get_rect()
    winTextRect.center = ((320 // 2) + 200, (320 // 2) - 50)
    winTextRect2.center = ((320 // 2) + 200, 320 // 2)

    while win_credits:  
        screen.blit(win_text, winTextRect)
        screen.blit(win_text2, winTextRect2)
        pg.display.update()

        for event in pg.event.get():
            if event.type == pg.QUIT:
                pg.quit()
            if event.type == pg.KEYDOWN:
                if event.key == pg.K_g:
                    # reloads another maze and resets all variables
                    global maze
                    maze = np.loadtxt(get_random_maze())
                    global cells
                    cells = np.ndarray((maze.shape[0], maze.shape[1], 3))
                    for i in range(maze.shape[0]): #repaint new maze
                        for j in range(maze.shape[1]):
                            if maze[i][j] == 0:
                                cells[i][j] = color_dict[0]
                            else:
                                cells[i][j] = color_dict[1]

                    cells[0][0] = color_dict[3] # add our player block
         
                    global WIDTH
                    WIDTH = cells.shape[0] * cellsize
                    global HEIGHT
                    HEIGHT = cells.shape[1] * cellsize
                    
                    global surf
                    surf = pg.Surface((cells.shape[0], cells.shape[1]))
                    global counter
                    counter = 0
                    
                    pg.surfarray.blit_array(surf, cells)
                    
                    surf = pg.transform.scale(surf, (WIDTH, HEIGHT))
                    screen = pg.display.set_mode((WIDTH, HEIGHT))
                    screen.blit(surf, (0, 0))
                    pg.display.update()
                    win_credits = False



###############
#             #
#  Game Loop  #
#             #
###############

running = True
win_credits = False
# Starting player pos and values
player_pos = (0,0)
start_square = (-1,-1) #invalid squares to start
end_square = (-1,-1)
is_start_pressed = False


screen = pg.display.set_mode((720, 320))

while not is_start_pressed: # welcoming message
    screen.fill((200,200,255)) 
    screen.blit(welcome_text, welcomeTextRect)
    screen.blit(welcome_text2, welcomeTextRect2)
    screen.blit(welcome_text3, welcomeTextRect3)
    pg.display.update()

    for event in pg.event.get():
        if event.type == pg.QUIT:
            pg.quit()
        if event.type == pg.KEYDOWN:
            if event.key == pg.K_RETURN:
                is_start_pressed = True

#main maze loop
while running:
    screen = pg.display.set_mode((WIDTH, HEIGHT))
    clock.tick(60)
  
    for event in pg.event.get():
        if event.type == pg.QUIT:
            running = False

        # get mouse presses and calculates shortest path
        if event.type == pg.MOUSEBUTTONDOWN: 
            if counter == 0:
                pos = pg.mouse.get_pos()
                start_square = click_to_coord(pos)
                counter += 1
                cells[start_square[0]][start_square[1]] = color_dict[2]

                #update surface array
                surf = pg.Surface((cells.shape[0], cells.shape[1]))
                pg.surfarray.blit_array(surf, cells)
                surf = pg.transform.scale(surf, (WIDTH, HEIGHT))

            elif counter == 1:
                pos = pg.mouse.get_pos()
                end_square = click_to_coord(pos)
                counter += 1
                cells[end_square[0]][end_square[1]] = color_dict[2]
                path = a_star(start_square, end_square, maze)
                for i in path:
                    cells[i[0]][i[1]] = color_dict[2] #color all paths squares as red
            else:
                continue 

        if event.type == pg.KEYDOWN:
            pos_to_color = player_move(event.key, player_pos) # check for a player move
            cells[pos_to_color[0]][pos_to_color[1]] = color_dict[3]
            if player_pos != pos_to_color:
                cells[player_pos[0]][player_pos[1]] = color_dict[0]
            player_pos = pos_to_color
            if pos_to_color[0] == maze.shape[0]-1 and pos_to_color[1] == maze.shape[1]-1:
                player_pos = (0,0)
                cells[pos_to_color[0]][pos_to_color[1]] = color_dict[0]
                cells[player_pos[0]][player_pos[1]] = color_dict[3]
                win_credits = True
                show_win_credits(win_credits)
                

            # Update surface array for pygame visualization
            surf = pg.Surface((cells.shape[0], cells.shape[1]))
            pg.surfarray.blit_array(surf, cells)
            surf = pg.transform.scale(surf, (WIDTH, HEIGHT))

            if event.key == pg.K_RETURN:
                surf = pg.Surface((cells.shape[0], cells.shape[1]))
                pg.surfarray.blit_array(surf, cells)
                surf = pg.transform.scale(surf, (WIDTH, HEIGHT))
            if event.key == pg.K_r:
                counter = 0
                clean_maze()

          
    # blit the transformed surface onto the screen
    screen.blit(surf, (0, 0))
    pg.display.update()

                
pg.quit()