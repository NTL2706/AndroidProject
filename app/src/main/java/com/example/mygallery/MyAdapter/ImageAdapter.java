package com.example.mygallery.MyAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygallery.MyFragment.PhotoFragment;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.MainActivity;
import com.example.mygallery.mainActivities.PictureActivity;
import com.example.mygallery.models.Category;
import com.example.mygallery.models.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context context;
    private PhotoFragment photoFragment;
    private List<Image> listImages;
    private List<Category> listCategory;

    private Intent intent;
    private ArrayList<String> listPath ;
    private ArrayList<String> listThumb ;
    private List<Image>multiPick;
    public ImageAdapter(Context context){
        this.context = context;
    }

    public void setData(List<Image> listImages, PhotoFragment photoFragment,List<Image>multiPick) {
        this.listImages = listImages;
        this.photoFragment = photoFragment;
        this.multiPick= multiPick;
        notifyDataSetChanged();
    }
    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image,parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = listImages.get(position);
        if (image == null)
            return;

        Glide.with(context).load(image.getThumb()).into(holder.imgPhoto);
        if (PhotoFragment.selected){
            holder.checkBox.setVisibility(CheckBox.VISIBLE);
            holder.checkBox.setChecked(false);
        }
        else {
            holder.checkBox.setVisibility(CheckBox.GONE);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    multiPick.add(image);
                }else {
                    multiPick.remove(image);
                }
            }
        });
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, PictureActivity.class);
                MyAsyncTask myAsyncTask =new MyAsyncTask();
                myAsyncTask.setPos(position);
                myAsyncTask.execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listImages != null) {
            return listImages.size();
        }
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPhoto;
        private CheckBox checkBox;
        private MainActivity main;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto= itemView.findViewById(R.id.Photo);
            checkBox =itemView.findViewById(R.id.checkedImage);

            imgPhoto.setOnLongClickListener(photoFragment);
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        public int pos;

        public void setPos(int pos) {
            this.pos = pos;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listPath = new ArrayList<>();
            listThumb = new ArrayList<>();
            for(int i = 0;i<listCategory.size();i++) {
                List<Image> listGirl = listCategory.get(i).getListGirl();
                for (int j = 0; j < listGirl.size(); j++) {
                    listPath.add(listGirl.get(j).getPath());
                    listThumb.add(listGirl.get(j).getThumb());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            intent.putStringArrayListExtra("data_list_path", listPath);
            intent.putStringArrayListExtra("data_list_thumb", listThumb);
            intent.putExtra("pos", listPath.indexOf(listImages.get(pos).getPath()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
