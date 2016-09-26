package giift.com.formrapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.giift.formr.field.Text;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Text text = (Text) findViewById(R.id.text);
    text.SetText("test");

  }


}
