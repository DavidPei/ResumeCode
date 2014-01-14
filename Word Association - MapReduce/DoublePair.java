/*
 * CS 61C Fall 2013 Project 1
 *
 * DoublePair.java is a class which stores two doubles and 
 * implements the Writable interface. It can be used as a 
 * custom value for Hadoop. To use this as a key, you can
 * choose to implement the WritableComparable interface,
 * although that is not necessary for credit.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class DoublePair implements WritableComparable {
    double d1;
    double d2;

    /**
     * Constructs a DoublePair with both doubles set to zero.
     */
    public DoublePair() {
        this.d1 = 0.0;
	this.d2 = 0.0;
    }

    /**
     * Constructs a DoublePair containing double1 and double2.
     */ 
    public DoublePair(double double1, double double2) {
        this.d1 = double1;
	this.d2 = double2;
    }

    /**
     * Returns the value of the first double.
     */
    public double getDouble1() {
        return this.d1;
    }

    /**
     * Returns the value of the second double.
     */
    public double getDouble2() {
        return this.d2;
    }

    /**
     * Sets the first double to val.
     */
    public void setDouble1(double val) {
        this.d1 = val;
    }

    /**
     * Sets the second double to val.
     */
    public void setDouble2(double val) {
	this.d2 = val;
    }

    /**
     * write() is required for implementing Writable.
     */
    public void write(DataOutput out) throws IOException {
	out.writeDouble(this.d1);
	out.writeDouble(this.d2);
    }

    /**
     * readFields() is required for implementing Writable.
     */
    public void readFields(DataInput in) throws IOException {
	this.d1 = in.readDouble();
	this.d2 = in.readDouble();
    }

    public int compareTo(Object dp) { //might need to change behavior of compareTo based on what it is used for. This is the basic behavior.
	double thisD1 = ((DoublePair)this).getDouble1();
	double thisD2 = ((DoublePair)this).getDouble2();
	double dpD1 = ((DoublePair)dp).getDouble1();
	double dpD2 = ((DoublePair)dp).getDouble2();
    
	if (thisD1 < dpD1) {
	    return -1;
	} else if (thisD1 > dpD1) {
	    return 1;
	} else {
	    if (thisD2 < dpD2) {
		return -1;
	    } else if (thisD2 > dpD2) {
		return 1;
	    } else {
		return 0;
	    }
	}	    
    }

    public int hashCode() {
	return (int) (java.lang.Math.pow(new Double(this.getDouble1()).hashCode(), new Double(this.getDouble2()).hashCode()));
    }

    public static void main(String[] args) {
	System.out.println("Creating a new DoublePair, dp,  with no set values.");
	DoublePair dp = new DoublePair();
	System.out.println("The first value of dp is " + dp.getDouble1() + ", which should be 0.0 .");
	System.out.println("The second value of dp is " + dp.getDouble2() + ", which should be 0.0 .");

	System.out.println("");

	System.out.println("Changing the values of this dp to 2.12345 and 7.12345, for d1 and d2 respectively.");
	dp.setDouble1(2.12345);
	dp.setDouble2(7.12345);
	System.out.println("The first value of dp is " + dp.getDouble1() + ", which should be 2.12345.");
	System.out.println("The second value of dp is " + dp.getDouble2() + ", which should be 7.12345");

	System.out.println("");

	System.out.println("Creating a new DoublePair, dp1, with set values 7.12345, 2.12345.");
	DoublePair dp1 = new DoublePair(7.12345, 2.12345);
	System.out.println("The first value of dp is " + dp1.getDouble1() + ", which should be 7.12345");
	System.out.println("The second value of dp is " + dp1.getDouble2() + ", which should be 2.12345");

	System.out.println("");

	System.out.println("Testing compareTo()...");
	System.out.println("dp.compareTo(dp1) should be -1: " + dp.compareTo(dp1));
	System.out.println("dp1.compareTo(dp) should be 1: " + dp1.compareTo(dp));
	System.out.println("dp2, the newly created DoublePair identical to dp, should be true when compared to dp.");
	DoublePair dp2 = new DoublePair(2.12345, 7.12345);
	System.out.println("dp2.compareTo(dp) should be equal or 0: " + dp2.compareTo(dp));
	System.out.println("dp.compareTo(dp2) should be equal or 0: " + dp.compareTo(dp2));
	System.out.println("dp3, the newly created DoublePair with no values, should be false when compared to any of the dp's.");
	DoublePair dp3 = new DoublePair();
	System.out.println("dp3.compareTo(dp) should be -1: " + dp3.compareTo(dp));
	System.out.println("dp.compareTo(dp3) should be 1: " + dp.compareTo(dp3));

	System.out.println("");

	System.out.println("Testing hashCode()...");
	System.out.println("dp.hashCode() : " + dp.hashCode());
	System.out.println("dp1.hashCode() : " + dp1.hashCode());
	System.out.println("dp2.hashCode() : " + dp2.hashCode());
	System.out.println("dp3.hashCode() : " + dp3.hashCode());
	System.out.println("dp and dp2 will have the same hashCode.");

	//How to test readFields() and write()?
    }
}
