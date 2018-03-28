package in.nishachar.anand.weather.home;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.nishachar.anand.weather.R;
import in.nishachar.anand.weather.models.ForecastDay;
import in.nishachar.anand.weather.shared.GlideApp;

/**
 * Created by anand on 13/03/18.
 */

public class ForeCastRecyclerAdapter extends RecyclerView.Adapter<ForeCastRecyclerAdapter.ForeCastViewHolder> {

    private List<ForecastDay> forecastDays;

    ForeCastRecyclerAdapter(List<ForecastDay> forecastDays) {
        this.forecastDays = forecastDays;
    }

    @Override
    public ForeCastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForeCastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForeCastViewHolder holder, int position) {
        holder.bindData(forecastDays.get(position));
    }

    @Override
    public int getItemCount() {
        return forecastDays.size();
    }

    static class ForeCastViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.dateText)
        AppCompatTextView dateText;

        @BindView(R.id.statusText)
        AppCompatTextView statusText;

        @BindView(R.id.weatherImage)
        AppCompatImageView weatherImage;

        @BindView(R.id.highTempText)
        AppCompatTextView highTempText;

        @BindView(R.id.lowTempText)
        AppCompatTextView lowTempText;

        @BindString(R.string.temp_format)
        String tempFormat;

        ForeCastViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bindData(ForecastDay forecastDay){

            SimpleDateFormat receivedFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH);

            try {
                dateText.setText(newFormat.format(receivedFormat.parse(forecastDay.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            statusText.setText(forecastDay.getDay().getCondition().getText());
            highTempText.setText(String.format(tempFormat, forecastDay.getDay().getMaxtempC()));
            lowTempText.setText(String.format(tempFormat, forecastDay.getDay().getMintempC()));

            GlideApp.with(itemView.getContext())
                    .load("http:" + forecastDay.getDay().getCondition().getIcon())
                    .into(weatherImage);
        }
    }
}
