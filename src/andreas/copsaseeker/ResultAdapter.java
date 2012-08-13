package andreas.copsaseeker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultAdapter extends ArrayAdapter<Result>{

    Context context; 
    int layoutResourceId;    
    ArrayList<Result> data = null;
    
    public ResultAdapter(Context context, int layoutResourceId, ArrayList<Result> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ResultHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ResultHolder();
            holder.txtDesde = (TextView)row.findViewById(R.id.desde);
            holder.txtHacia = (TextView)row.findViewById(R.id.hacia);
            holder.txtSalida = (TextView)row.findViewById(R.id.salida);
            holder.txtPor = (TextView)row.findViewById(R.id.recorrido);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ResultHolder)row.getTag();
        }
        
        Result result = data.get(position);
        holder.txtDesde.setText(result.desde);
        holder.txtHacia.setText(result.hacia);
        holder.txtSalida.setText(result.salida);
        holder.txtPor.setText(result.por);
        
        return row;
    }
    
    static class ResultHolder
    {
        TextView txtDesde;
        TextView txtHacia;
        TextView txtSalida;
        TextView txtPor;
    }
}