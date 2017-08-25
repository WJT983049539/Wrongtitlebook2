package xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FixGridLayout extends ViewGroup {
	private int mCellWidth;
	private int mCellHeight;

	/**
	 * �����ӿؼ��Ļ���
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int cellWidth = mCellWidth;
		int cellHeight = mCellHeight;
		int columns = (r - l) / cellWidth;
		if (columns < 0) {
			columns = 1;
		}
		int x = 0;
		int y = 0;
		int i = 0;
		int count = getChildCount();
		int rows=count/columns;
		if(rows<=0){
			rows=1;
		}else{
			if(count%columns>0){
				rows++;
			}
		}
//		this.layout(l, t, l+r, t+b*rows);
		for (int j = 0; j < count; j++) {
			final View childView = getChildAt(j);
			// ��ȡ�ӿؼ�Child�Ŀ��
			int w = childView.getMeasuredWidth();
			int h = childView.getMeasuredHeight();
			// �����ӿؼ��Ķ�������
			int left = x + ((cellWidth - w) / 2);
			int top = y + ((cellHeight - h) / 2);
			// int left = x;
			// int top = y;
			// �����ӿؼ�
			childView.layout(left, top, left + w, top + h);

			if (i >= (columns - 1)) {
				i = 0;
				x = 0;
				y += cellHeight;
			} else {
				i++;
				x += cellWidth;
			}
		}
	}

	/**
	 * ����ؼ����ӿؼ���ռ����
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ������������
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST);
		// ��¼ViewGroup��Child���ܸ���
		int count = getChildCount();
		// �����ӿռ�Child�Ŀ��
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			childView.measure(cellWidthSpec, cellHeightSpec);
		}
		// ���������ؼ���ռ�����С
		int measuredWidth = measureWidth(widthMeasureSpec);//�����
		//���㼸��
		int maxColumns=measuredWidth/mCellWidth;
		int rows=count/maxColumns;
		if(rows<=0){
			rows=1;
		}else{
			if(count%maxColumns>0){
				rows++;
			}
		}
		// ע��setMeasuredDimension��resolveSize���÷�
		setMeasuredDimension(measuredWidth,
				resolveSize(mCellHeight*rows, heightMeasureSpec));
//		 setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//		int measuredHeight = measureHeight(heightMeasureSpec);
//		setMeasuredDimension(measuredWidth,measuredHeight);
		// ����Ҫ���ø���ķ���
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	private int measureHeight(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.

		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {

			// Calculate the ideal size of your
			// control within this maximum size.
			// If your control fills the available
			// space return the outer bound.

			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {

			// If your control can fit within these bounds return that
			// value.
			result = specSize;
		}

		return result;
	}

	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			// Calculate the ideal size of your control
			// within this maximum size.
			// If your control fills the available space
			// return the outer bound.
			result = specSize;
		}

		else if (specMode == MeasureSpec.EXACTLY) {
			// If your control can fit within these bounds return that
			// value.

			result = specSize;
		}

		return result;
	}
	/**
	 * Ϊ�ؼ���ӱ߿�
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
//		// ��ȡ���ֿؼ����
//		int width = getWidth();
//		int height = getHeight();
//		// ��������
//		Paint mPaint = new Paint();
//		// ���û��ʵĸ�������
//		mPaint.setColor(Color.BLUE);
//		mPaint.setStyle(Paint.Style.STROKE);
//		mPaint.setStrokeWidth(10);
//		mPaint.setAntiAlias(true);
//		// �������ο�
//		Rect mRect = new Rect(0, 0, width, height);
//		// ���Ʊ߿�
//		canvas.drawRect(mRect, mPaint);
//		// ��������ø���ķ���
		super.dispatchDraw(canvas);
	}

	public FixGridLayout(Context context) {
		super(context);
	}

	public FixGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FixGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setmCellWidth(int w) {
		mCellWidth = w;
		requestLayout();
	}

	public void setmCellHeight(int h) {
		mCellHeight = h;
		requestLayout();
	}

}