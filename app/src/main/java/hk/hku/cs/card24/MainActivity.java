package hk.hku.cs.card24;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.Jep;
import com.singularsys.jep.ParseException;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] data;
    int[] card;
    int[] imageClickCount;
    ImageButton[] cards;
    Button rePick;
    Button validateInput;
    Button clear;
    Button openBracket;
    Button closeBracket;
    Button subtract;
    Button add;
    Button multiply;
    Button divide;
    TextView userExpression;
    String lastSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastSelectedItem = "";
        cards = new ImageButton[4];
        imageClickCount = new int[4];
        validateInput = (Button) findViewById(R.id.checkinput);
        validateInput.setEnabled(false);
        validateInput.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                String userExp = userExpression.getText().toString();
                if (checkInput(userExp)){
                    Toast.makeText(MainActivity.this, "Correct Answer!!", Toast.LENGTH_LONG).show();
                    pickCard();
                }
                else{
                    Toast.makeText(MainActivity.this, "Incorrect Answer :((", Toast.LENGTH_LONG).show();
                    setClear();
                }
            }
        });
        cards[0] = (ImageButton) findViewById(R.id.card1);
        cards[0].setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View V){
                clickCard(0);
                lastSelectedItem = "card";
                validateButtonStatus();

            }
        });
        cards[1] = (ImageButton) findViewById(R.id.card2);
        cards[1].setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View V){
                clickCard(1);
                lastSelectedItem = "card";
                validateButtonStatus();


            }
        });
        cards[2] = (ImageButton) findViewById(R.id.card3);
        cards[2].setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View V){
                clickCard(2);
                lastSelectedItem = "card";
                validateButtonStatus();


            }
        });
        cards[3] = (ImageButton) findViewById(R.id.card4);
        cards[3].setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View V){
                clickCard(3);
                lastSelectedItem = "card";
                validateButtonStatus();


            }
        });
        initCardImages();
        rePick = (Button) findViewById(R.id.repick);
        rePick.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                pickCard();
            }
        });
        openBracket = (Button) findViewById(R.id.left);
        openBracket.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                userExpression.append("(");
                lastSelectedItem = "(";
                validateButtonStatus();


            }
        });
        closeBracket = (Button) findViewById(R.id.right);
        closeBracket.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                userExpression.append(")");
                lastSelectedItem = ")";
                validateButtonStatus();
            }
        });
        subtract = (Button) findViewById(R.id.minus);
        subtract.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                userExpression.append("-");
                lastSelectedItem = "-";
                validateButtonStatus();
            }
        });
        add = (Button) findViewById(R.id.plus);
        add.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                userExpression.append("+");
                lastSelectedItem = "+";
                validateButtonStatus();
            }
        });
        multiply = (Button) findViewById(R.id.multiply);
        multiply.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                userExpression.append("*");
                lastSelectedItem = "*";
                validateButtonStatus();

            }
        });
        divide = (Button) findViewById(R.id.divide);
        divide.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                userExpression.append("/");
                lastSelectedItem = "/";
                validateButtonStatus();


            }
        });
        userExpression = (TextView) findViewById(R.id.input);
        pickCard();
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V){
                setClear();
            }
        });

        validateButtonStatus();

    }

    private boolean isArithmeticSymbol(String symbol){
        return symbol == "+" || symbol == "-" || symbol == "*" || symbol == "/";
    }

    private void validateButtonStatus(){
        //cards
        for(int i = 0; i < cards.length; i++){
            if(lastSelectedItem == "" || lastSelectedItem == "(" || isArithmeticSymbol(lastSelectedItem)){
                cards[i].setEnabled(true);
            }
            else{
                cards[i].setEnabled(false);
            }
        }
        //open bracket
        if(lastSelectedItem == "" || lastSelectedItem == "(" || isArithmeticSymbol(lastSelectedItem)){
            openBracket.setEnabled(true);
        }
        else{
            openBracket.setEnabled(false);
        }

        //arithmetic symbols and close bracket

        if(lastSelectedItem == "card" || lastSelectedItem == ")"){
            setSymbols(true);
        }
        else{
            setSymbols(false);
        }

    }

    private void setSymbols(boolean val){
        subtract.setEnabled(val);
        add.setEnabled(val);
        divide.setEnabled(val);
        multiply.setEnabled(val);
        closeBracket.setEnabled(val);
    }

    private boolean checkInput(String input){
        Jep jep = new Jep();
        Object res;
        try {
            jep.parse(input);
            res = jep.evaluate();
        }catch(ParseException p) {
            p.printStackTrace();
            Toast.makeText(MainActivity.this, "Incorrect Expression", Toast.LENGTH_LONG).show();
            return false;
        }catch(EvaluationException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Incorrect Expression", Toast.LENGTH_LONG).show();
            return false;
        }
        Double value = (Double)res;
        if (Math.abs(value-24) < 1e-6)
            return true;
        return false;
    }

    private void clickCard(int i){
        int resId;
        String num;
        Integer value;
        if (imageClickCount[i] == 0){
            resId = getResources().getIdentifier("back_0", "drawable", "hk.hku.cs.card24");
            cards[i].setImageResource(resId);
            cards[i].setClickable(false);
            value = data[i];
            num = value.toString();
            userExpression.append(num);
            imageClickCount[i]++;
        }

        checkValidateInputStatus();
    }

    private void checkValidateInputStatus(){
        int ctr = 0;
        for(int i = 0 ;i < imageClickCount.length; i++){
            if(imageClickCount[i] != 0)
                ctr ++;
        }
        if(ctr == 4)
            validateInput.setEnabled(true);
    }

    private void pickCard(){
        data = new int[4];
        card = new int[4];
        int min = 1;
        int max = 52;
        for(int i =0; i < 4; i++){
            int randNum = (int)Math.floor(Math.random()*(max-min+1)+min);
            card[i] = randNum;
            data[i] = randNum % 13;
            if (data[i] == 0) {data[i] = 13;}
        }
        setClear();
    }

    private void setClear(){
        int resId;
        validateInput.setEnabled(false);
        userExpression.setText("");
        for(int i = 0; i < 4;i++){
            imageClickCount[i] = 0;
            resId = getResources().getIdentifier("card"+card[i], "drawable", "hk.hku.cs.card24");
            cards[i].setImageResource(resId);
            cards[i].setClickable(true);
        }
        lastSelectedItem = "";
        validateButtonStatus();

    }

    private void initCardImages(){
        int imageId = getResources().getIdentifier("back_0", "drawable", "hk.hku.cs.card24");
        System.out.println(imageId);
        for(int i = 0; i < 4; i++){
            cards[i].setImageResource(imageId);
        }
    }

}