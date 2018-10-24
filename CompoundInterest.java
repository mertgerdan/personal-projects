import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
* Mert Gerdan Compound Interest Calculator
* 01/2017?
*/

public class CompoundInterest extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	public JLabel savingsLabel;
	public JLabel yearsLabel;
	public JLabel cRateLabel;
	public JLabel iRateLabel;
	public JLabel finalValueLabel;
	public JLabel compTypeLabel;

	public static String savings = "Savings: ";
	public static String years = "Years: ";
	public static String cRate = "Average market return rate: ";
	public static String iRate = "Average inflation rate: ";
	public static String finalValue = "Generated value:";
	public static String compType = "Times compounded in a year: ";

	public JFormattedTextField savingsField;
	public JFormattedTextField yearsField;
	public JFormattedTextField cRateField;
	public JFormattedTextField iRateField;
	public JFormattedTextField finalValueField;
	public JFormattedTextField compTypeField;

	public static double savings1 = 1;
	public static double years1 = 1;
	public static double cRate1 = 1;
	public static double iRate1 = 1;
	public static double final1 = 0;
	public static double compType1 = 12;

	public CompoundInterest() {
		super(new BorderLayout()); // constructor

		savingsLabel = new JLabel(savings);
		yearsLabel = new JLabel(years);
		cRateLabel = new JLabel(cRate);
		iRateLabel = new JLabel(iRate);
		finalValueLabel = new JLabel(finalValue);
		compTypeLabel = new JLabel(compType);

		savingsField = new JFormattedTextField(savings1);
		savingsField.setColumns(20);
		savingsField.addPropertyChangeListener("value", this);

		yearsField = new JFormattedTextField(years1);
		yearsField.setColumns(20);
		yearsField.addPropertyChangeListener("value", this);

		cRateField = new JFormattedTextField(cRate1);
		cRateField.setColumns(20);
		cRateField.addPropertyChangeListener("value", this);

		iRateField = new JFormattedTextField(iRate1);
		iRateField.setColumns(20);
		iRateField.addPropertyChangeListener("value", this);
		
		compTypeField = new JFormattedTextField(compType1);
		compTypeField.setColumns(20);
		compTypeField.addPropertyChangeListener("value", this);

		finalValueField = new JFormattedTextField(final1);
		finalValueField.setValue(new Double(final1));
		finalValueField.setColumns(10);
		finalValueField.setForeground(Color.RED);
		finalValueField.setEditable(false);

		// this sets it into a nice grid that I can't be bothered to make myself
		JPanel labelPane = new JPanel(new GridLayout(0, 1));
		labelPane.add(savingsLabel);
		labelPane.add(yearsLabel);
		labelPane.add(cRateLabel);
		labelPane.add(iRateLabel);
		labelPane.add(finalValueLabel);
		labelPane.add(compTypeLabel);

		JPanel fieldPane = new JPanel(new GridLayout(0, 1));
		fieldPane.add(savingsField);
		fieldPane.add(yearsField);
		fieldPane.add(cRateField);
		fieldPane.add(iRateField);
		fieldPane.add(finalValueField);
		fieldPane.add(compTypeField);

		setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		add(labelPane, BorderLayout.CENTER);
		add(fieldPane, BorderLayout.LINE_END);

	}

	
	public static double CompInterest(double starting, double year, double cRate, double compTypeYear) {
		double amount = starting * Math.pow((1 + ((cRate/100)/compTypeYear)),compTypeYear*year); //(1+(1/x))^xt
		return amount;
	}
	
	
	public static double InflationCalc(double money, double year, double iRate) { // same
		money -= money / 100 * iRate;
		int b = 1;
		if (b != year) {
			return InflationCalc(money, year - 1, iRate);
		} else {
			return money;
		}
	}

	public static void guiCreate() { // this calls the Main method
		JFrame frame = new JFrame("Compound Interest Calculator v3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new CompoundInterest());
		frame.pack();
		frame.setVisible(true);
	}

	// checks for a change in an input, and adjusts final1 accordingly
	public void propertyChange(PropertyChangeEvent event) {
		Object src = event.getSource();
		if (src == savingsField) {
			savings1 = ((Number) savingsField.getValue()).doubleValue();
		} else if (src == yearsField) {
			years1 = ((Number) yearsField.getValue()).doubleValue();
		} else if (src == cRateField) {
			cRate1 = ((Number) cRateField.getValue()).doubleValue();
		} else if (src == iRateField) {
			iRate1 = ((Number) iRateField.getValue()).doubleValue();
		} else if (src == compTypeField) {
			compType1 = ((Number) compTypeField.getValue()).doubleValue();
		}

		final1 = InflationCalc(CompInterest(savings1, years1, cRate1, compType1), years1, iRate1);
		;
		finalValueField.setValue(new Double(final1));
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				guiCreate();
			}
		});

	}

}
