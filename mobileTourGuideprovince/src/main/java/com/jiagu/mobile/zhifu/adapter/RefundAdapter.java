package com.jiagu.mobile.zhifu.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.zhifu.RefundActivity;
import com.jiagu.mobile.zhifu.bean.Refund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RefundAdapter extends BaseAdapter {
	private ArrayList<Refund> data;
	private Context context;
	private LayoutInflater inflater;
	private TextView text;
	private BigDecimal jiage = new BigDecimal("0");

	public RefundAdapter(ArrayList<Refund> data, Context context, TextView text) {
		super();
		this.data = data;
		this.context = context;
		this.text = text;
		jiage = new BigDecimal(text.getText().toString());
		inflater = LayoutInflater.from(context);
	}

	public void setData(ArrayList<Refund> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("size" + data.size());
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.activity_refund_listview_item, null);
			holder.text = (TextView) convertView
					.findViewById(R.id.activity_refund_listview_item_text);
			holder.type = (TextView) convertView
					.findViewById(R.id.activity_refund_listview_item_type);
			holder.mCheckBox = (ImageView) convertView
					.findViewById(R.id.activity_refund_listview_item_checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Refund refund = data.get(position);
		holder.text.setText(refund.getNumber());
		if (refund.getIsuse().equals("0")) {
			holder.type.setText("未消费");
			holder.mCheckBox.setVisibility(View.VISIBLE);
			holder.mCheckBox.setImageResource(R.drawable.shoucang);
		} else if (refund.getIsuse().equals("1")) {
			holder.type.setText("已消费");
			holder.type.setPadding(0, 0, 15, 0);
			holder.mCheckBox.setVisibility(View.GONE);
		} else if (refund.getIsuse().equals("2")) {
			holder.type.setText("退款中");
			holder.type.setPadding(0, 0, 15, 0);
			holder.mCheckBox.setVisibility(View.GONE);
		}else if (refund.getIsuse().equals("3")) {
			holder.type.setText("已退款");
			holder.type.setPadding(0, 0, 15, 0);
			holder.mCheckBox.setVisibility(View.GONE);
		}
		if (data.get(position).getChecked()) {
			holder.mCheckBox.setImageResource(R.drawable.shoucang);

		}else{	
			holder.mCheckBox.setImageResource(R.drawable.shoucang1);			
		}
		
		if (RefundActivity.refundActivity.paytype.equals("1")) {
			if (!data.get(position).getChecked()) {
				data.get(position).setChecked(true);
				jiage = jiage.add(new BigDecimal(data.get(position).getPrice()));
				text.setText(jiage + "");
				holder.mCheckBox.setImageResource(R.drawable.shoucang);
				holder.mCheckBox.setClickable(false);
			}				
	    }
		else{
		final ImageView iamge = holder.mCheckBox;
		holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!data.get(position).getChecked()) {
					data.get(position).setChecked(true);
					jiage = jiage.add(new BigDecimal(refund.getPrice()));
					iamge.setImageResource(R.drawable.shoucang);
				} else {
					data.get(position).setChecked(false);
					jiage = jiage.subtract(new BigDecimal(refund.getPrice()));
					iamge.setImageResource(R.drawable.shoucang1);
				}
				text.setText(jiage + "");
			}
		});
		}
		return convertView;
	}

	class ViewHolder {
		public TextView text, type;
		public ImageView mCheckBox;
	}

	public ArrayList<Refund> getData() {
		return data;
	}

	public String getJiage() {
		return jiage + "";
	}
}
