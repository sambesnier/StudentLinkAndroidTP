package com.example.sambesnier.studentlink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sambesnier.studentlink.R;
import com.example.sambesnier.studentlink.models.Vote;

import java.util.List;

/**
 * Created by Administrateur on 31/08/2017.
 */

public class VoteAdapter extends ArrayAdapter<Vote> {

    public VoteAdapter(Context context, List<Vote> votes) {
        super(context, 0, votes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_vote,parent, false);
        }

        VoteViewHolder viewHolder = (VoteViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new VoteViewHolder();
            viewHolder.question = (TextView) convertView.findViewById(R.id.questionRow);
            viewHolder.username = (TextView) convertView.findViewById(R.id.usernameRow);
            convertView.setTag(viewHolder);
        }

        Vote vote = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.question.setText(vote.getQuestion());
        viewHolder.username.setText(vote.getUsername());

        return convertView;
    }

    private class VoteViewHolder{
        public TextView username;
        public TextView question;
    }

}
