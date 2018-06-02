package com.jiagu.mobile.zhifu.adapter;

import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.MyImageLoader;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.zhifu.bean.Article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Article> data;
	private LayoutInflater inflater;
	private String name,adress,phonenumber,cargoclassify;

	public ArrayList<Article> getData() {
		return data;
	}

	public void setData(ArrayList<Article> data) {
		this.data = data;
	}

	public ArticleAdapter(Context context, ArrayList<Article> data,
			String name, String adress, String phonenumber, String cargoclassify) {
		super();
		this.context = context;
		this.data = data;
		this.name = name;
		this.adress = adress;
		this.phonenumber = phonenumber;
		this.cargoclassify = cargoclassify;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
			convertView = inflater.inflate(R.layout.activity_article_listview_item, null);
			holder.text = (TextView) convertView
					.findViewById(R.id.activity_article_listview_item_tv_01);
			holder.text03 = (TextView) convertView
					.findViewById(R.id.activity_article_listview_item_tv_03);
			holder.text04 = (TextView) convertView
					.findViewById(R.id.activity_article_listview_item_tv_04);
			holder.text05 = (TextView) convertView
					.findViewById(R.id.activity_article_listview_item_tv_05);
			holder.text06 = (TextView) convertView
					.findViewById(R.id.activity_article_listview_item_tv_06);
			holder.text02 = (ImageView) convertView
					.findViewById(R.id.activity_article_listview_item_tv_02);
			holder.image = (ImageView) convertView
					.findViewById(R.id.activity_article_listview_item_iv);
			holder.text07 = (TextView) convertView
					.findViewById(R.id.activity_article_listview_item_tv_where);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Article article = data.get(position);
		holder.text.setText("劵码:"+article.getNumber());
		if(article.getImageurl()!=null){
			MyImageLoader.loadImage(Path.IMAGER_ADRESS+article.getImageurl(), holder.image);
		}
		holder.text03.setText("["+name+"]");
		holder.text04.setText("地址:["+adress+"]");
		holder.text05.setText("电话:["+phonenumber+"]");
		if ("2".equals(cargoclassify.trim())) {
			holder.text07.setText("兑换方法:请在[自我游平台]或商家前台兑换");
		}else {
			holder.text07.setText("兑换方法:请在[手机导游平台]或商家前台兑换");
		}
		
		final ImageView image = holder.text02;
		if (data.get(position).getChecked()) {
			image.setImageResource(R.drawable.shoucang);
		}else{
			image.setImageResource(R.drawable.shoucang1);
		}
		holder.text02.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (data.get(position).getChecked()) {
					image.setImageResource(R.drawable.shoucang1);
					data.get(position).setChecked(false);
				}else{
					image.setImageResource(R.drawable.shoucang);
					data.get(position).setChecked(true);
				}
			}
		});
		if (article.getIsuse().equals("0")) {
			holder.text06.setText("未消费");
			holder.text02.setVisibility(View.VISIBLE);
		}else if (article.getIsuse().equals("1")){
			holder.text06.setText("已消费");
			holder.text02.setVisibility(View.GONE);
		}else if (article.getIsuse().equals("2")){
			holder.text06.setText("退款中");
			holder.text02.setVisibility(View.GONE);
		}else if (article.getIsuse().equals("3")){
			holder.text06.setText("已退款");
			holder.text02.setVisibility(View.GONE);
		}
		return convertView;
	}
	class ViewHolder {
		public TextView text,text03,text04,text05,text06,text07;
		public ImageView image,text02;
	}
}
