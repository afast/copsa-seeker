package andreas.copsaseeker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TimePicker;

public class CopsaseekerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void filter(View v){
    	Intent intent = new Intent(CopsaseekerActivity.this, Results.class);
    	Bundle b = new Bundle();
    	b.putString("linea", ((Spinner)findViewById(R.id.linea)).getSelectedItem().toString());
    	b.putString("desde", ((Spinner)findViewById(R.id.desde)).getSelectedItem().toString());
    	b.putString("hacia", ((Spinner)findViewById(R.id.hacia)).getSelectedItem().toString());
    	b.putString("por", ((Spinner)findViewById(R.id.por)).getSelectedItem().toString());
    	b.putInt("fromHour", ((TimePicker)findViewById(R.id.from)).getCurrentHour());
    	b.putInt("fromMinute", ((TimePicker)findViewById(R.id.from)).getCurrentMinute());
    	b.putInt("toHour", ((TimePicker)findViewById(R.id.to)).getCurrentHour());
    	b.putInt("toMinute", ((TimePicker)findViewById(R.id.to)).getCurrentMinute());
    	intent.putExtras(b);
    	startActivity(intent);
    	finish();
    }
}