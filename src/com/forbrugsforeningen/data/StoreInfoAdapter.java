package com.forbrugsforeningen.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.forbrugsforeningen.R;

import java.util.List;

/**
 * User: vavaka
 * Date: 12/23/10 3:24 PM
 */
public class StoreInfoAdapter extends ArrayAdapter<StoreInfo> {
    private List<StoreInfo> items;

    public StoreInfoAdapter(Context context, int textViewResourceId, List<StoreInfo> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.row_result, null);
        }

        final StoreInfo storeInfo = items.get(position);
        if (storeInfo != null) {
            ((TextView) view.findViewById(R.id.label_row_results_name)).setText(storeInfo.name);
            ((TextView) view.findViewById(R.id.label_row_results_address)).setText(storeInfo.address);
            ((TextView) view.findViewById(R.id.label_row_results_postcode)).setText(storeInfo.postCode + " " + storeInfo.postName);

            SpannableString phoneText = new SpannableString(storeInfo.phone);
            phoneText.setSpan(new UnderlineSpan(), 0, phoneText.length(), 0);
            ((TextView) view.findViewById(R.id.label_row_results_phone)).setText(phoneText);

            ((TextView) view.findViewById(R.id.label_row_results_discount)).setText(storeInfo.discount);

            SpannableString webSiteText = new SpannableString("WWW");
            webSiteText.setSpan(new UnderlineSpan(), 0, webSiteText.length(), 0);
            ((TextView) view.findViewById(R.id.label_row_results_website)).setText(webSiteText);

            view.findViewById(R.id.label_row_results_phone).setClickable(true);
            view.findViewById(R.id.label_row_results_phone).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try {
                       Intent intent = new Intent(Intent.ACTION_CALL);
                       intent.setData(Uri.parse("tel:+45" + storeInfo.phone));
                       StoreInfoAdapter.this.getContext().startActivity(intent);
                    } catch (Exception e) {
                        System.out.println("Failed to invoke call to: " + storeInfo.phone);
                        e.printStackTrace();
                    }

                }
            });

            view.findViewById(R.id.label_row_results_website).setClickable(true);
            view.findViewById(R.id.label_row_results_website).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeInfo.webSite));
                    StoreInfoAdapter.this.getContext().startActivity(browserIntent);
                }
            });
        }

        return view;
    }
}
