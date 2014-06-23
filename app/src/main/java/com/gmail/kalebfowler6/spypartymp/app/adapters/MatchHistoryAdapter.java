package com.gmail.kalebfowler6.spypartymp.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gmail.kalebfowler6.spypartymp.app.R;
import com.gmail.kalebfowler6.spypartymp.app.models.Round;
import com.gmail.kalebfowler6.spypartymp.app.views.RoundScoreView;

import java.util.List;

/**
 * Created by stuart on 6/22/14.
 */
public class MatchHistoryAdapter extends ArrayAdapter<Round> {

    public MatchHistoryAdapter(Context context, List<Round> rounds) {
        super(context, android.R.layout.simple_list_item_1, rounds);
    }

    @Override
    public Round getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.include_match_history_row, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView.findViewById(R.id.rootView);
            holder.playerName = (TextView) convertView.findViewById(R.id.playerName);
            holder.roundScore = (RoundScoreView) convertView.findViewById(R.id.roundScore);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Round round = getItem(position);

        holder.playerName.setText(round.getRoundNumber() + ". Spy: " + round.getSpyPlayer());
        holder.roundScore.setScore(round.getRoundScore(), round.getPlayerRole());

        if ((getCount() - position - 1) % 4 <= 1) {
            holder.rootView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        } else {
            holder.rootView.setBackgroundColor(getContext().getResources().getColor(R.color.light_gray));
        }

        return convertView;
    }

    private class ViewHolder {
        private View rootView;
        private TextView playerName;
        private RoundScoreView roundScore;
    }

}
