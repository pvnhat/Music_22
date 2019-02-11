package com.framgia.music_22.screen.home_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.framgia.music_22.utils.Constant;
import com.framgia.vnnht.music_22.R;

public class SlidePagerAdapter extends PagerAdapter implements View.OnClickListener {

    private String[] mImages = {
            Constant.BANNER_ITEM1, Constant.BANNER_ITEM2, Constant.BANNER_ITEM3,
            Constant.BANNER_ITEM4, Constant.BANNER_ITEM5, Constant.BANNER_ITEM6
    };
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public SlidePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mLayoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.slide_item_pager, container, false);
        ImageButton imageButtonSlideItem = itemView.findViewById(R.id.imagebutton_slide_item);
        Glide.with(mContext).load(mImages[position]).into(imageButtonSlideItem);
        imageButtonSlideItem.setOnClickListener(this);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(mContext, R.string.string_onclick_item, Toast.LENGTH_SHORT).show();
    }
}
