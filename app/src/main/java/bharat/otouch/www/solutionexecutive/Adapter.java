package bharat.otouch.www.solutionexecutive;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by shaan on 12/07/2017.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<UserData> data= Collections.emptyList();
    UserData current;
    int currentPos=0;
    // create constructor to innitilize context and data sent from MainActivity
    public Adapter(Context context, List<UserData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.user_query, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
         current=data.get(position);
        myHolder.textFishName.setText("UserName" +current.UserName);
        myHolder.textType.setText("UserQuery" + current.UserQuery);
        // load image into imageview using glide


//        ((MyHolder) holder).onClick(new View.OnClickListener());
        ((MyHolder) holder).textFishName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),OderDetail.class);
               // intent.putExtra("img",current.fishImage);
                intent.putExtra("name",current.UserName);
               // Log.d("TAG","name"+current.UserName);
                intent.putExtra("email",current.UserEmail);
                intent.putExtra("mobile",current.UserMobile);
                intent.putExtra("query",current.UserQuery);
                intent.putExtra("fcmtoken",current.Fcmtoken);
              //  Toast.makeText(context, "check value"+current.UserEmail+" "+current.UserMobile, Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(intent);
            }
        });
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textFishName;
        TextView textType;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.textFishName);
           // ivFish= (ImageView) itemView.findViewById(R.id.ivFish);
            // textSize = (TextView) itemView.findViewById(R.id.textSize);
            textType = (TextView) itemView.findViewById(R.id.textType);
            //  textPrice = (TextView) itemView.findViewById(R.id.textPrice);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

        }
    }

}

