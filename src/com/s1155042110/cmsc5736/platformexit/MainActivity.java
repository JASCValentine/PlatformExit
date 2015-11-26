package com.s1155042110.cmsc5736.platformexit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private String packageName;
	
	private Spinner spnLine;
	private Spinner spnStation;
	private TextView tvSelectedStation;
	private TextView tvPlatform1;
	private TextView tvPlatform2;
	private ListView lvPlatform1;
	private ListView lvPlatform2;
	private View emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		packageName = getPackageName();
		getViewReference();
		
		spnLine.setOnItemSelectedListener(spnOnItemSelectedListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_about) {
			showAboutDialog();			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void getViewReference() {
		spnLine = (Spinner) findViewById(R.id.spnLine);
		spnStation = (Spinner) findViewById(R.id.spnStation);
		tvSelectedStation = (TextView) findViewById(R.id.tvSelectedStation);
		tvPlatform1 = (TextView) findViewById(R.id.tvPlatform1);
		tvPlatform2 = (TextView) findViewById(R.id.tvPlatform2);
		lvPlatform1 = (ListView) findViewById(R.id.lvPlatform1);
		lvPlatform2 = (ListView) findViewById(R.id.lvPlatform2);		
		emptyView = findViewById(R.id.tvEmpty);
	}
	
	private AdapterView.OnItemSelectedListener spnOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			int resourceId;			
			
			if (position <= 0) {
				resourceId = R.array.spinner_select;
			} else {
				String[] values = parent.getResources().getStringArray(R.array.Lines_values);
				String selected = values[position];
				resourceId = getResources().getIdentifier(selected + "_Stations", "array", packageName);
			}
			
			if (resourceId == 0) resourceId = R.array.spinner_select;
			
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(parent.getContext(), resourceId, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnStation.setAdapter(adapter);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) { /* will not implement */ }
	};

	public void btnSubmit_onClick(View v) {
		if (spnLine.getSelectedItemPosition() <= 0 ||
			spnStation.getSelectedItemPosition() <= 0) {
			Toast.makeText(this, "Please select line and station.", Toast.LENGTH_SHORT).show();
			return;		
		}
		
		tvSelectedStation.setText((CharSequence) spnStation.getSelectedItem());
		
		Resources res = getResources();
		String line = res.getStringArray(R.array.Lines_values)[spnLine.getSelectedItemPosition()];
		String destinationFormat = res.getString(R.string.destination_format);
		tvPlatform1.setText(String.format(destinationFormat, res.getString(res.getIdentifier(line + "_destination1", "string", packageName))));
		tvPlatform2.setText(String.format(destinationFormat, res.getString(res.getIdentifier(line + "_destination2", "string", packageName))));
		
		String[] values = res.getStringArray(res.getIdentifier(line + "_Stations_values", "array", packageName));
		String selected = values[spnStation.getSelectedItemPosition()];
		
		int platform1ResourceId = res.getIdentifier(selected + "_1", "array", packageName);
		int platform2ResourceId = res.getIdentifier(selected + "_2", "array", packageName);
		
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, platform1ResourceId, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, platform2ResourceId, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		lvPlatform1.setAdapter(adapter1);
		lvPlatform2.setAdapter(adapter2);
		
		lvPlatform1.setEmptyView(emptyView);
		lvPlatform2.setEmptyView(emptyView);
	}
	
	private void showAboutDialog() {
		DialogFragment dlgf = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("About");
				builder.setMessage(Html.fromHtml(getResources().getString(R.string.about)));
				builder.setNegativeButton("Close", null);
				builder.setCancelable(false);
				return builder.create();
			}
		};
		dlgf.show(getFragmentManager(), "about");
	}
}
