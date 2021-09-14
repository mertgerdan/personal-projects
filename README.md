# personal-projects
This project is for Microsoft Imagine Cup 2018. It is a new way to possibly detect stolen work.

The machine learning model was only prototyped on Azure ML Studio, this version uses a hand-set activation threshold. 

## Overview
The main problem with plagiarism is that if someone is determined enough to plagiarize, they are determined enough to change the copied work slightly to not be caught by online plagiarism checkers. The idea behind this program was to find differences between paragraphs in terms of some parameters we found, such as:
- Words per sentence
- Unique word count
- Unique word complexity (How "sophisticated" a given word in a sentence is, we had a basic formula to find the sophistication of a word.)

## Setup
Setup is very straightforward as it only was a console application which was a prototype in our pitch. Nothing other than the JDK is required to run this program.

Who knows, I might come back and actually implement the machine learning aspect to the code and add more parameters if I run out of personal project ideas, it sounds fun.
