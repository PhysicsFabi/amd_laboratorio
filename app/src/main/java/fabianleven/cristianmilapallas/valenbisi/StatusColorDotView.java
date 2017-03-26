package fabianleven.cristianmilapallas.valenbisi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.EnumMap;

/**
 * Created by Fabi on 26.03.2017.
 */

public class StatusColorDotView extends android.support.v7.widget.AppCompatImageView {

    private final static Parte.STATUS[] NUMBER_TO_STATUS_MAP = {
            Parte.STATUS.OPEN,
            Parte.STATUS.IN_PROGRESS,
            Parte.STATUS.CLOSED
    };

    private static final EnumMap<Parte.STATUS, Integer> COLOR_MAP;

    static {
        COLOR_MAP = new EnumMap<Parte.STATUS, Integer>(Parte.STATUS.class);
        COLOR_MAP.put(Parte.STATUS.CLOSED, Color.GREEN);
        COLOR_MAP.put(Parte.STATUS.IN_PROGRESS, Color.YELLOW);
        COLOR_MAP.put(Parte.STATUS.OPEN, Color.RED);
    }

    private Parte.STATUS status;
    private ShapeDrawable circle;

    public StatusColorDotView(Context context) {
        super(context);
        init(context, null);
    }

    public StatusColorDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StatusColorDotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        int status_number = 0;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StatusColorDotView,
                0, 0);
        try {
            status_number = a.getInteger(R.styleable.StatusColorDotView_status, 0);
        } finally {
            a.recycle();
        }
        status = NUMBER_TO_STATUS_MAP[status_number];

        circle = new ShapeDrawable(new OvalShape());
        setBackground(circle);
        setStatus(status);
    }

    public void setStatus(Parte.STATUS status) {
        this.status = status;
        circle.getPaint().setColor(COLOR_MAP.get(status));
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        circle.setIntrinsicWidth(width);
        circle.setIntrinsicHeight(height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height= getMeasuredHeight();
        setMeasuredDimension(height, height);
    }

}
