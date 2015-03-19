package com.chaos.dmtipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

// main Activity class for the TipCalculator
public class MainActivity extends Activity
{
        // constants used when saving/restoring state
        private static final String BILL_TOTAL = "BILL_TOTAL";
        private static final String CUSTOM_PERCENT = "CUSTOM_PERCENT";
        private static final String CUSTOM_TAX="CUSTOM_TAX";
        private static final String HEADS="HEADS";
        private double currentBillTotal; // bill amount entered by the user
        private int currentCustomPercent; // tip % set with the SeekBar
        private int currentTaxPercent;//tax % set with the SeekBar
        private int currentHeads;//amount of people
        private EditText tip10EditText; // displays 10% tip
        private EditText total10EditText; // displays total with 10% tip
        private EditText tip15EditText; // displays 15% tip
        private EditText total15EditText; // displays total with 15% tip
        private EditText billEditText; // accepts user input for bill total
        private EditText tip20EditText; // displays 20% tip
        private EditText total20EditText;  // displays total with 20% tip
        private TextView customTipTextView; // displays custom tip percentage
        private TextView customTaxPercentTextView; //displays custom tax percentage
        private TextView customHeadsTextView;//current amount of people
        private EditText tipCustomEditText; // displays custom tip amount
        private EditText taxCustomEditText; //displays custom tax amount
        private EditText headsCustomEditText;//displays the amount of people
        private EditText totalCustomEditText; // displays total with custom tip
        private EditText totalPlusTaxCustomEditText; // displays total with custom tax
        private EditText totalEvenSplitCustomEditText;//displays the amount owed by each person individually

        // Called when the activity is first created.
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState); // call superclass's version
            setContentView(R.layout.activity_main); // inflate the GUI

            // check if app just started or is being restored from memory
            if ( savedInstanceState == null ) // the app just started running
            {
                currentBillTotal = 0.0; // initialize the bill amount to zero
                currentCustomPercent = 18; // initialize the custom tip to 18%
                currentTaxPercent=0; //initialize the custom tax to 0%
                currentHeads=0;
            } // end if
            else // app is being restored from memory, not executed from scratch
            {
                // initialize the bill amount to saved amount
                currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);

                // initialize the custom tip and tax to saved tip percent and tax percent
                currentCustomPercent =
                        savedInstanceState.getInt(CUSTOM_PERCENT);
                currentTaxPercent=
                        savedInstanceState.getInt(CUSTOM_TAX);
                currentHeads=
                        savedInstanceState.getInt(HEADS);
            } // end else

            // get references to the 10%, 15% and 20% tip and total EditTexts
            tip10EditText = (EditText) findViewById(R.id.tip10EditText);
            total10EditText = (EditText) findViewById(R.id.total10EditText);
            tip15EditText = (EditText) findViewById(R.id.tip15EditText);
            total15EditText = (EditText) findViewById(R.id.total15EditText);
            tip20EditText = (EditText) findViewById(R.id.tip20EditText);
            total20EditText = (EditText) findViewById(R.id.total20EditText);

            // get the TextView displaying the custom tip percentage
            customTipTextView = (TextView) findViewById(R.id.customTipTextView);

            //get the TextView displaying the custom tax percentage
            customTaxPercentTextView=(TextView) findViewById(R.id.customTaxPercentTextView);
            //get the TextView displaying the custom heads
            customHeadsTextView=(TextView) findViewById(R.id.customHeadsTextView);
            // get the custom tip and total EditTexts
            tipCustomEditText = (EditText) findViewById(R.id.tipCustomEditText);
            totalCustomEditText =
                    (EditText) findViewById(R.id.totalCustomEditText);
            // get the custom tax and tax+total EditTexts
            taxCustomEditText=(EditText) findViewById(R.id.taxCustomEditText);
            totalPlusTaxCustomEditText=(EditText) findViewById(R.id.totalPlusTaxCustomEditText);
            //get the customHeads edit text
            headsCustomEditText=(EditText) findViewById(R.id.HeadsCustomEditText);
            //get the total split between the heads editText
            totalEvenSplitCustomEditText= (EditText) findViewById(R.id.EvenSplitCustomEditText);
            // get the billEditText
            billEditText = (EditText) findViewById(R.id.billEditText);

            // billEditTextWatcher handles billEditText's onTextChanged event
            billEditText.addTextChangedListener(billEditTextWatcher);

            // get the SeekBar used to set the custom tip amount
            SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
            customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
            SeekBar customTaxSeekBar=(SeekBar) findViewById(R.id.customTaxSeekBar);
            customTaxSeekBar.setOnSeekBarChangeListener(customTaxSeekBarListener);
            SeekBar customHeadsSeekBar=(SeekBar) findViewById(R.id.customHeadsSeekBar);
            customHeadsSeekBar.setOnSeekBarChangeListener(customHeadsSeekBarListener);
        } // end method onCreate

        // updates 10, 15 and 20 percent tip EditTexts
    private void updateStandard()
    {
        // calculate bill total with a ten percent tip
        double tenPercentTip = currentBillTotal * .1;
        double tenPercentTotal = currentBillTotal + tenPercentTip;

        // set tipTenEditText's text to tenPercentTip
        tip10EditText.setText(String.format("%.02f", tenPercentTip));

        // set totalTenEditText's text to tenPercentTotal
        total10EditText.setText(String.format("%.02f", tenPercentTotal));

        // calculate bill total with a fifteen percent tip
        double fifteenPercentTip = currentBillTotal * .15;
        double fifteenPercentTotal = currentBillTotal + fifteenPercentTip;

        // set tipFifteenEditText's text to fifteenPercentTip
        tip15EditText.setText(String.format("%.02f", fifteenPercentTip));

        // set totalFifteenEditText's text to fifteenPercentTotal
        total15EditText.setText(
                String.format("%.02f", fifteenPercentTotal));

        // calculate bill total with a twenty percent tip
        double twentyPercentTip = currentBillTotal * .20;
        double twentyPercentTotal = currentBillTotal + twentyPercentTip;

        // set tipTwentyEditText's text to twentyPercentTip
        tip20EditText.setText(String.format("%.02f", twentyPercentTip));

        // set totalTwentyEditText's text to twentyPercentTotal
        total20EditText.setText(String.format("%.02f", twentyPercentTotal));
    } // end method updateStandard

    // updates the custom tip and total EditTexts
    private void updateCustom()
    {
        // set customTipTextView's text to match the position of the SeekBar
        customTipTextView.setText(currentCustomPercent + "%");
        // set customTaxTextView's text to match the position of the SeekBar
        customTaxPercentTextView.setText(currentTaxPercent+"%");
        //set customHeadsTextView's text to match the position of the SeekBar
        customHeadsTextView.setText(currentHeads+"%");
        // calculate the custom tip amount
        double customTipAmount =
                currentBillTotal * currentCustomPercent * .01;
        // calculate the custom tax amount
        double customTaxAmount=
                currentBillTotal*currentTaxPercent*.01;
        // calculate the custom heads amount
        double customHeadsAmount=currentHeads*1.0;
        // calculate the total bill, including the custom tip
        double customTotalAmount = currentBillTotal + customTipAmount;
        // calculate the total bill, including the custom tax;
        double customTotalPlusTaxAmount=customTotalAmount+(customTaxAmount);
        // calculate the total split between the heads
        double customSplitAmount=customTotalPlusTaxAmount/customHeadsAmount;

        // display the tip and total bill amounts
        tipCustomEditText.setText(String.format("%.02f", customTipAmount));
        totalCustomEditText.setText(
                String.format("%.02f", customTotalAmount));
        //display the heads
        headsCustomEditText.setText(String.format("%.02f",customHeadsAmount));
        //display the tax and total bill plus tax amounts
        taxCustomEditText.setText(String.format("%.02f", customTaxAmount));
        totalPlusTaxCustomEditText.setText(String.format("%.02f", customTotalPlusTaxAmount));
        //display the total split evenly between the number of heads
        totalEvenSplitCustomEditText.setText(String.format("%.02f", customSplitAmount));

    } // end method updateCustom

    // save values of billEditText and customSeekBar
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putDouble(BILL_TOTAL, currentBillTotal);
        outState.putInt(CUSTOM_PERCENT, currentCustomPercent);
        outState.putInt(CUSTOM_TAX, currentTaxPercent);
    } // end method onSaveInstanceState

    // called when the user changes the position of SeekBar
    private OnSeekBarChangeListener customTaxSeekBarListener =
            new OnSeekBarChangeListener()
            {
                // update currentCustomPercent, then call updateCustom
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser)
                {
                    // sets currentCustomPercent to position of the SeekBar's thumb
                    currentTaxPercent = seekBar.getProgress();
                    updateCustom(); // update EditTexts for custom tip and total
                } // end method onProgressChanged

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                } // end method onStartTrackingTouch

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                } // end method onStopTrackingTouch
            }; // end OnSeekBarChangeListener
    private OnSeekBarChangeListener customSeekBarListener =
            new OnSeekBarChangeListener()
            {
                // update currentCustomPercent, then call updateCustom
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser)
                {
                    // sets currentCustomPercent to position of the SeekBar's thumb
                    currentCustomPercent = seekBar.getProgress();
                    updateCustom(); // update EditTexts for custom tip and total
                } // end method onProgressChanged

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                } // end method onStartTrackingTouch

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                } // end method onStopTrackingTouch
            }; // end OnSeekBarChangeListener
    private OnSeekBarChangeListener customHeadsSeekBarListener =
            new OnSeekBarChangeListener()
            {
                // update currentCustomPercent, then call updateCustom
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser)
                {
                    // sets currentCustomPercent to position of the SeekBar's thumb
                    currentHeads = seekBar.getProgress();
                    updateCustom(); // update EditTexts for custom tip and total
                } // end method onProgressChanged

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                } // end method onStartTrackingTouch

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                } // end method onStopTrackingTouch
            }; // end OnSeekBarChangeListener
    // event-handling object that responds to billEditText's events
    private TextWatcher billEditTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert billEditText's text to a double
            try
            {
                currentBillTotal = Double.parseDouble(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                currentBillTotal = 0.0; // default if an exception occurs
            } // end catch

            // update the standard and custom tip EditTexts
            updateStandard(); // update the 10, 15 and 20% EditTexts
            updateCustom(); // update the custom tip EditTexts
        } // end method onTextChanged

        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end billEditTextWatcher
} // end class TipCalculator
