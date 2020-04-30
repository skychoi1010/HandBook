package com.basic.appbasiclibs.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.appbasiclibs.R;

import java.util.ArrayList;
import java.util.List;


public class CustomShareAdapter extends ArrayAdapter<ResolveInfo>
{
    private PackageManager pm=null;
    Context context;
    
    ArrayList<String> app_name=new ArrayList<String>();
    
    public CustomShareAdapter(Context ctx, PackageManager pm, List<ResolveInfo> apps)
    {
        super(ctx, R.layout.custom_share_list, apps);
        this.pm=pm;
        context=ctx;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView==null)
        {
            convertView=newView(parent);
        }
      
        bindView(position, convertView);
      
        return(convertView);
    }
    
    private View newView(ViewGroup parent)
    {
	   LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	   return(li.inflate(R.layout.custom_share_list, parent, false));
    }
    
    public ArrayList<String> getAppName()
    {
		return app_name;
	}
    
    public String get_app_name(int position)
    {
    	return ""+getItem(position).loadLabel(pm);
    }

    private void bindView(int position, View row)
    {
        TextView label=(TextView)row.findViewById(R.id.label);
        ImageView icon=(ImageView)row.findViewById(R.id.icon);

        app_name.add(""+getItem(position).loadLabel(pm));

        label.setText(getItem(position).loadLabel(pm));
        icon.setImageDrawable(getItem(position).loadIcon(pm));
    }
}
