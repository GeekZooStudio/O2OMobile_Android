//
//       _/_/_/                      _/            _/_/_/_/_/
//    _/          _/_/      _/_/    _/  _/              _/      _/_/      _/_/
//   _/  _/_/  _/_/_/_/  _/_/_/_/  _/_/              _/      _/    _/  _/    _/
//  _/    _/  _/        _/        _/  _/          _/        _/    _/  _/    _/
//   _/_/_/    _/_/_/    _/_/_/  _/    _/      _/_/_/_/_/    _/_/      _/_/
//
//
//  Copyright (c) 2015-2016, Geek Zoo Studio
//  http://www.geek-zoo.com
//
//
//  Permission is hereby granted, free of charge, to any person obtaining a
//  copy of this software and associated documentation files (the "Software"),
//  to deal in the Software without restriction, including without limitation
//  the rights to use, copy, modify, merge, publish, distribute, sublicense,
//  and/or sell copies of the Software, and to permit persons to whom the
//  Software is furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//  IN THE SOFTWARE.
//

package com.insthub.O2OMobile.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BeeFramework.Utils.AnimationUtil;
import com.BeeFramework.Utils.TimeUtil;
import com.BeeFramework.Utils.Utils;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.MyDialog;
import com.BeeFramework.view.RoundedWebImageView;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.OrderInfoModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ENUM_ORDER_STATUS;
import com.insthub.O2OMobile.Protocol.ENUM_PAY_CODE;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.Protocol.SIMPLE_USER;
import com.insthub.O2OMobile.Protocol.orderacceptResponse;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;
import com.insthub.O2OMobile.Utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class D1_OrderActivity extends BaseActivity implements BusinessResponse, IXListViewListener {

    private AnimationDrawable mAnimationDrawable = null;
    private Dialog mPriceDialog;
    private Dialog mOrderDialog;
    private ImageView mBack;
    private TextView mTitle;
    private TextView mRightText;
    private XListView mXListView;
    private View mHeaderView;
    private Button mOrderBtnCancel;
    private Button mOrderBtnOk;
    private ImageView mOrderEmptyView;
    private RoundedWebImageView mOrderEmployerAvatar;
    private TextView mOrderEmployerName;
    private ImageView mOrderEmployerPhone;
    private TextView mOrderTime;
    private TextView mOrderSn;
    private TextView mOrderServiceType;
    private TextView mOrderAppointmentTime;
    private TextView mOrderLocation;
    private TextView mOrderPrice;
    private LinearLayout mOrderTransactionPriceView;
    private TextView mOrderTransactionPrice;
    private TextView mOrderContentText;
    private LinearLayout mOrderContentVoiceView;
    private ImageView mOrderContentVoiceIcon;
    private TextView mOrderContentVoiceTime;
    private LinearLayout mOrderStatusView;
    private TextView mOrderStatus;
    private ImageView mOrderStatusArrow;
    private View mOrderVisibleButtonView;
    private LinearLayout mOrderWaitView;
    private TextView mOrderEmployeeCount;
    private LinearLayout mOrderEmployeeView;
    private RoundedWebImageView mOrderEmployeeAvatar;
    private TextView mOrderEmployeeName;
    private ImageView mOrderEmployeePhone;
    private LinearLayout mOrderCommentView;
    private View mOrderPlay;
    private LinearLayout mOrderPlayOrderView;
    private TextView mOrderPlayOrderSn;
    private TextView mOrderPlayOrderServiceType;
    private TextView mOrderPlayOrderLocation;
    private TextView mOrderPlayOrderPrice;
    private LinearLayout mOrderPlayButtonView;
    private Button mOrderPlayOnline;
    private Button mOrderPlayOffline;
    private Button mOrderPlayCancel;
    private TextView mOrderPriceDialogPrice;
    private EditText mOrderPriceDialogChangePrice;
    private Button mOrderPriceDialogOk;
    private Button mOrderPriceDialogCancel;
    protected ImageLoader mImageLoader = ImageLoader.getInstance();
    private OrderInfoModel mOrderInfoModel;
    private int mOrderId;
    public static String ORDER_ID = "orderId";
    private MediaPlayer mPlayer;
    private int mWidthPixels; //屏幕宽度
    private ENUM_PAY_CODE payCode;
    public int COMMENT_SEND = 1;
    private int PLAY_VOICE = 1;
    private TextView mAcceptOrderTime;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d1_order_listview);
		
		mBack = (ImageView) findViewById(R.id.top_view_back);
		mTitle = (TextView) findViewById(R.id.top_view_title);
		mRightText = (TextView) findViewById(R.id.top_view_right_text);
		mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
		mTitle.setText(getString(R.string.order_detail));
		mRightText.setVisibility(View.VISIBLE);
		
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		mWidthPixels = dm.widthPixels;
		
		Intent intent = getIntent();
		mOrderId = intent.getIntExtra(ORDER_ID, 0);
		
		mHeaderView = LayoutInflater.from(this).inflate(R.layout.d1_order, null);
		mXListView = (XListView) findViewById(R.id.order_listview);
		mXListView.addHeaderView(mHeaderView);
        mXListView.setPullLoadEnable(false);
        mXListView.loadMoreHide();
		mXListView.setRefreshTime();
		mXListView.setXListViewListener(this, 1);
		mXListView.setAdapter(null);
		
		mOrderBtnCancel = (Button) 				findViewById(R.id.order_button_cancel);
		mOrderBtnOk = (Button) 				findViewById(R.id.order_button_ok);
		mOrderEmptyView = (ImageView) 			findViewById(R.id.order_empty_view);
		
		mOrderPlay = 						findViewById(R.id.order_play);
		mOrderPlayOrderView = (LinearLayout) 		findViewById(R.id.order_play_order_view);
		mOrderPlayOrderSn = (TextView) 			findViewById(R.id.order_play_order_sn);
		mOrderPlayOrderServiceType = (TextView) 			findViewById(R.id.order_play_order_service_type);
		mOrderPlayOrderLocation = (TextView) 			findViewById(R.id.order_play_order_location);
		mOrderPlayOrderPrice = (TextView) 			findViewById(R.id.order_play_order_price);
		mOrderPlayButtonView = (LinearLayout) 		findViewById(R.id.order_play_button_view);
		mOrderPlayOnline = (Button) 				findViewById(R.id.order_play_online);
		mOrderPlayOffline = (Button) 				findViewById(R.id.order_play_offline);
		mOrderPlayCancel = (Button) 				findViewById(R.id.order_play_cancel);

		mOrderEmployerAvatar = (RoundedWebImageView) mHeaderView.findViewById(R.id.order_employer_avatar);
		mOrderEmployerName = (TextView) 			mHeaderView.findViewById(R.id.order_employer_name);
		mOrderEmployerPhone = (ImageView) 			mHeaderView.findViewById(R.id.order_employer_phone);
		mOrderTime = (TextView) 			mHeaderView.findViewById(R.id.order_time);
		mOrderSn = (TextView) 			mHeaderView.findViewById(R.id.order_sn);
		mOrderServiceType = (TextView) 			mHeaderView.findViewById(R.id.order_service_type);
		mOrderAppointmentTime = (TextView) 			mHeaderView.findViewById(R.id.order_appointment_time);
		mOrderLocation = (TextView) 			mHeaderView.findViewById(R.id.order_location);
		mOrderPrice = (TextView) 			mHeaderView.findViewById(R.id.order_price);
		mOrderTransactionPriceView = (LinearLayout) 		mHeaderView.findViewById(R.id.order_transaction_price_view);
		mOrderTransactionPrice = (TextView) 			mHeaderView.findViewById(R.id.order_transaction_price);
		mOrderContentText = (TextView) 			mHeaderView.findViewById(R.id.order_content_text);
		mOrderContentVoiceView = (LinearLayout) 		mHeaderView.findViewById(R.id.order_content_voice_view);
		mOrderContentVoiceIcon = (ImageView) 			mHeaderView.findViewById(R.id.order_content_voice_icon);
		mOrderContentVoiceTime = (TextView) 			mHeaderView.findViewById(R.id.order_content_voice_time);
		mOrderStatusView = (LinearLayout) 		mHeaderView.findViewById(R.id.order_status_view);
		mOrderStatus = (TextView) 			mHeaderView.findViewById(R.id.order_status);
        mOrderStatusArrow = (ImageView)           mHeaderView.findViewById(R.id.order_status_arrow);
		mOrderVisibleButtonView = 						mHeaderView.findViewById(R.id.order_visible_button_view);
		mOrderWaitView = (LinearLayout) 		mHeaderView.findViewById(R.id.order_wait_view);
		mOrderEmployeeCount = (TextView) 			mHeaderView.findViewById(R.id.order_employee_count);
		mOrderEmployeeView = (LinearLayout) 		mHeaderView.findViewById(R.id.order_employee_view);
		mOrderEmployeeAvatar = (RoundedWebImageView) mHeaderView.findViewById(R.id.order_employee_avatar);
		mOrderEmployeeName = (TextView) 			mHeaderView.findViewById(R.id.order_employee_name);
		mOrderEmployeePhone = (ImageView) 			mHeaderView.findViewById(R.id.order_employee_phone);
		mOrderCommentView = (LinearLayout) 		mHeaderView.findViewById(R.id.order_comment_view);
        mAcceptOrderTime = (TextView)            mHeaderView.findViewById(R.id.accept_order_time);
		
		mRightText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(D1_OrderActivity.this, G0_ReportActivity.class);
                intent.putExtra("orderId", mOrderId);
                startActivity(intent);
            }
        });
		mOrderEmptyView.setOnClickListener(null);
		mOrderPlayOrderView.setOnClickListener(null);
		mOrderPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AnimationUtil.backAnimationFromBottom(mOrderPlayOrderView);
                mOrderPlayOrderView.setVisibility(View.GONE);
                AnimationUtil.backAnimation(mOrderPlayButtonView);
                mOrderPlayButtonView.setVisibility(View.GONE);
                Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mOrderPlay.setVisibility(View.GONE);
                        mOrderBtnCancel.setVisibility(View.VISIBLE);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 200);
            }
        });
		
		mOrderPlayOnline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                payCode = ENUM_PAY_CODE.PAY_ONLINE;
                mOrderInfoModel.pay(mOrderId, ENUM_PAY_CODE.PAY_ONLINE.value());

            }
        });
		
		mOrderPlayOffline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                payCode = ENUM_PAY_CODE.PAY_OFFLINE;
                mOrderInfoModel.pay(mOrderId, ENUM_PAY_CODE.PAY_OFFLINE.value());
            }
        });
		
		mOrderPlayCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AnimationUtil.backAnimationFromBottom(mOrderPlayOrderView);
                mOrderPlayOrderView.setVisibility(View.GONE);
                AnimationUtil.backAnimation(mOrderPlayButtonView);
                mOrderPlayButtonView.setVisibility(View.GONE);
                Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mOrderPlay.setVisibility(View.GONE);
                        mOrderBtnCancel.setVisibility(View.VISIBLE);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 200);
            }
        });
		
		mOrderStatusView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(D1_OrderActivity.this, D2_OrderHistoryActivity.class);
                intent.putExtra("order_id", mOrderId);
                startActivity(intent);
            }
        });
		
		mOrderInfoModel = new OrderInfoModel(this);
		mOrderInfoModel.addResponseListener(this);
		mOrderInfoModel.get(mOrderId);
		
	}
	

    private void showOrderPriceDialog() {
		LayoutInflater inflater = LayoutInflater.from(D1_OrderActivity.this);
		View view = inflater.inflate(R.layout.d1_order_price_dialog, null);
		mPriceDialog = new Dialog(D1_OrderActivity.this, R.style.dialog);
		mPriceDialog.setContentView(view);
		mPriceDialog.setCanceledOnTouchOutside(false);
		mPriceDialog.show();
		
		mOrderPriceDialogPrice = (TextView) 	view.findViewById(R.id.order_price_dialog_price);
		mOrderPriceDialogChangePrice = (EditText) 	view.findViewById(R.id.order_price_dialog_change_price);
		mOrderPriceDialogOk = (Button) 		view.findViewById(R.id.order_price_dialog_ok);
		mOrderPriceDialogCancel = (Button) 		view.findViewById(R.id.order_price_dialog_cancel);
		
		if(mOrderInfoModel.publicOrder.offer_price != null) {
			mOrderPriceDialogPrice.setText(Utils.formatBalance(mOrderInfoModel.publicOrder.offer_price) + "元");
		}
		
		mOrderPriceDialogChangePrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() > 0) {
                    if (s.toString().substring(0, 1).equals(".")) {
                        s = s.toString().substring(1, s.length());
                        mOrderPriceDialogChangePrice.setText(s);
                    }
                }
                if (s.toString().length() > 1) {
                    if (s.toString().substring(0, 1).equals("0")) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            s = s.toString().substring(1, s.length());
                            mOrderPriceDialogChangePrice.setText(s);
                            CharSequence charSequencePirce = mOrderPriceDialogChangePrice.getText();
                            if (charSequencePirce instanceof Spannable) {
                                Spannable spanText = (Spannable) charSequencePirce;
                                Selection.setSelection(spanText, charSequencePirce.length());
                            }
                        }
                    }
                }
                boolean flag = false;
                for (int i = 0; i < s.toString().length() - 1; i++) {
                    String getstr = s.toString().substring(i, i + 1);
                    if (getstr.equals(".")) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    int i = s.toString().indexOf(".");
                    if (s.toString().length() - 3 > i) {
                        String getstr = s.toString().substring(0, i + 3);
                        mOrderPriceDialogChangePrice.setText(getstr);
                        CharSequence charSequencePirce = mOrderPriceDialogChangePrice.getText();
                        if (charSequencePirce instanceof Spannable) {
                            Spannable spanText = (Spannable) charSequencePirce;
                            Selection.setSelection(spanText, charSequencePirce.length());
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
		
		mOrderPriceDialogCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPriceDialog.dismiss();
            }
        });
		
		mOrderPriceDialogOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mOrderInfoModel.done(mOrderId, mOrderPriceDialogChangePrice.getText().toString());
                mPriceDialog.dismiss();
            }
        });
		
	}
	
	private void showOrderDialog(boolean isSucceed, orderacceptResponse response) {
		LayoutInflater inflater = LayoutInflater.from(D1_OrderActivity.this);
		View view = inflater.inflate(R.layout.d1_order_dialog, null);
		mOrderDialog = new Dialog(D1_OrderActivity.this, R.style.dialog);
		mOrderDialog.setContentView(view);
		mOrderDialog.setCanceledOnTouchOutside(false);
		mOrderDialog.show();
		
		Button 		order_dialog_button = 		(Button) 	view.findViewById(R.id.order_dialog_button);
		ImageView 	order_dialog_icon = 		(ImageView) view.findViewById(R.id.order_dialog_icon);
		TextView 	order_dialog_text = 		(TextView) 	view.findViewById(R.id.order_dialog_text);
		TextView    order_dialog_error_text = 	(TextView) view.findViewById(R.id.order_dialog_error_text);
		
		if(isSucceed) {
			order_dialog_icon.setImageResource(R.drawable.b2_selected_icon);
			order_dialog_text.setText(getString(R.string.receive_order_success));
			order_dialog_text.setTextColor(Color.parseColor("#39BCED"));
		} else {
			order_dialog_icon.setImageResource(R.drawable.d3_failed);
			order_dialog_text.setText(getString(R.string.receive_order_fail));
			order_dialog_text.setTextColor(Color.parseColor("#f65858"));
			order_dialog_error_text.setText(response.error_desc);
		}
		
		order_dialog_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mOrderDialog.dismiss();
			}
		});
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// TODO Auto-generated method stub
		mXListView.stopRefresh();
		if(url.endsWith(ApiInterface.ORDER_INFO)) {

            Message msg = new Message();
            msg.what = MessageConstant.RECEIVE_ORDER_PUSH;
            EventBus.getDefault().post(msg);

			if(jo != null) {
				mOrderEmptyView.setVisibility(View.GONE);
				mOrderEmptyView.setImageDrawable(null);
				mOrderEmptyView.setOnClickListener(null);
				setOrderDetailView(mOrderInfoModel.publicOrder);
			} else {
				mOrderEmptyView.setVisibility(View.VISIBLE);
				mOrderEmptyView.setImageResource(R.drawable.e7_no_connections);
				mOrderEmptyView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mOrderInfoModel.get(mOrderId);
                    }
                });
			}
		} else if(url.endsWith(ApiInterface.ORDER_CANCEL)) {
			ToastView toast = new ToastView(D1_OrderActivity.this, getString(R.string.order_have_canceled));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
			setOrderDetailView(mOrderInfoModel.publicOrder);
		} else if(url.endsWith(ApiInterface.ORDER_ACCEPT)) {
			orderacceptResponse response = new orderacceptResponse();
            response.fromJson(jo);
            if(response.succeed == 1) {
            	showOrderDialog(true, response);
            } else {
            	showOrderDialog(false, response);
            }
			setOrderDetailView(mOrderInfoModel.publicOrder);
		} else if(url.endsWith(ApiInterface.ORDER_WORK_DONE)) {
			setOrderDetailView(mOrderInfoModel.publicOrder);
		} else if(url.endsWith(ApiInterface.ORDER_PAY)) {

            if (payCode == ENUM_PAY_CODE.PAY_OFFLINE)
            {
                AnimationUtil.backAnimationFromBottom(mOrderPlayOrderView);
                mOrderPlayOrderView.setVisibility(View.GONE);
                AnimationUtil.backAnimation(mOrderPlayButtonView);
                mOrderPlayButtonView.setVisibility(View.GONE);
                Handler mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mOrderPlay.setVisibility(View.GONE);
                        mOrderBtnCancel.setVisibility(View.VISIBLE);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 200);
            }
            else
            {
              //Todo 在线支付
                ToastView toast = new ToastView(getApplicationContext(), getString(R.string.share_content));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

			setOrderDetailView(mOrderInfoModel.publicOrder);
		} else if(url.endsWith(ApiInterface.ORDER_CONFIRM_PAY)) {
			setOrderDetailView(mOrderInfoModel.publicOrder);
		}
	}
	
	private void setOrderDetailView(final ORDER_INFO order) {
		setOrderDate(order);

        mOrderEmployeeView.setVisibility(View.VISIBLE);
		if(order.order_status == ENUM_ORDER_STATUS.OS_PUBLISHED.value()) {
            mOrderEmployeeView.setVisibility(View.GONE);
			// 客户发单
			if(order.employer.id != 0) {
				if(isEmp(order.employer)) {
					mOrderWaitView.setVisibility(View.VISIBLE);
					mOrderEmployeeCount.setText(order.push_number + "");
					mOrderBtnCancel.setText(getString(R.string.cancel_order));
					mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            final MyDialog myDialog = new MyDialog(D1_OrderActivity.this, getString(R.string.cacel_order_or_not));
                            myDialog.show();
                            myDialog.positive.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    myDialog.dismiss();
                                    mOrderInfoModel.cancel(mOrderId);
                                }
                            });
                            myDialog.negative.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    myDialog.dismiss();
                                }
                            });
                        }
                    });
				} else {
					mOrderBtnCancel.setText(getString(R.string.receive_immediately));
					mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mOrderInfoModel.accept(mOrderId);
                        }
                    });
				}
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_KNOCK_DOWN.value()) {
			// 已确认接单

			mOrderWaitView.setVisibility(View.GONE);
			mOrderBtnCancel.setEnabled(true);
			mOrderBtnCancel.setBackgroundResource(R.drawable.c1_apply_btn_select);
			if(isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setEnabled(false);
				mOrderBtnCancel.setBackgroundResource(R.drawable.c1_applied_btn);
				mOrderBtnCancel.setText(getString(R.string.wait_for_complete));
			} else if(isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				mOrderBtnOk.setVisibility(View.VISIBLE);
				mOrderVisibleButtonView.setVisibility(View.VISIBLE);
				mOrderBtnOk.setText(getString(R.string.confirm_complete));
				mOrderBtnOk.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        showOrderPriceDialog();
                    }
                });
				mOrderBtnCancel.setText(getString(R.string.cancel_order));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final MyDialog myDialog = new MyDialog(D1_OrderActivity.this, getString(R.string.cacel_order_or_not));
                        myDialog.show();
                        myDialog.positive.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                myDialog.dismiss();
                                mOrderInfoModel.cancel(mOrderId);
                            }
                        });
                        myDialog.negative.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                myDialog.dismiss();
                            }
                        });
                    }
                });
			} else { // 游客
				setVisitor();
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_WORK_DONE.value()) {
			// 工作完成
			mOrderWaitView.setVisibility(View.GONE);
			mOrderBtnCancel.setEnabled(true);
			mOrderBtnCancel.setBackgroundResource(R.drawable.c1_apply_btn_select);
			mOrderTransactionPriceView.setVisibility(View.VISIBLE);
			if(order.transaction_price != null && !order.transaction_price.equals("")) {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.transaction_price) + "元");
			} else {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.offer_price) + "元");
			}
			if(isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setText(getString(R.string.immediately_pay));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mOrderPlay.setVisibility(View.VISIBLE);
                        mOrderPlayOrderView.setVisibility(View.VISIBLE);
                        AnimationUtil.showAnimationFromTop(mOrderPlayOrderView);
                        mOrderPlayButtonView.setVisibility(View.VISIBLE);
                        AnimationUtil.showAnimation(mOrderPlayButtonView);
                        mOrderBtnCancel.setVisibility(View.GONE);
                        if (order.order_sn != null) {
                            mOrderPlayOrderSn.setText(order.order_sn);
                        }
                        if (order.service_type != null && order.service_type.title != null) {
                            mOrderPlayOrderServiceType.setText(order.service_type.title);
                        }
                        if (order.location != null && order.location.name != null) {
                            mOrderPlayOrderLocation.setText(order.location.name);
                        }
                        mOrderPlayOrderPrice.setText(Utils.formatBalance(order.transaction_price) + "元");
                    }
                });
			} else if(isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				
				mOrderBtnOk.setVisibility(View.GONE);
				mOrderVisibleButtonView.setVisibility(View.GONE);
				mOrderBtnCancel.setEnabled(false);
				mOrderBtnCancel.setBackgroundResource(R.drawable.c1_applied_btn);
				mOrderBtnCancel.setText(getString(R.string.wait_for_pay));
			} else { // 游客
				setVisitor();
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_PAYED.value()) {
			// 已付款
			mOrderWaitView.setVisibility(View.GONE);
			mOrderBtnCancel.setEnabled(true);
			mOrderBtnCancel.setBackgroundResource(R.drawable.c1_apply_btn_select);
			mOrderTransactionPriceView.setVisibility(View.VISIBLE);
			if(order.transaction_price != null && !order.transaction_price.equals("")) {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.transaction_price) + getString(R.string.yuan));
			} else {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.offer_price) + getString(R.string.yuan));
			}
			if(isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setEnabled(false);
				mOrderBtnCancel.setBackgroundResource(R.drawable.c1_applied_btn);
				mOrderBtnCancel.setText(getString(R.string.wait_for_confirm_pay));
			} else if(isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				mOrderBtnCancel.setText(getString(R.string.confirm_pay));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mOrderInfoModel.confirmPay(mOrderId);
                    }
                });
			} else { // 游客
				setVisitor();
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_PAY_CONFORMED.value()) {
			// 付款已确认
			mOrderWaitView.setVisibility(View.GONE);
			mOrderBtnCancel.setEnabled(true);
			mOrderBtnCancel.setBackgroundResource(R.drawable.c1_apply_btn_select);
			mOrderTransactionPriceView.setVisibility(View.VISIBLE);
			if(order.transaction_price != null && !order.transaction_price.equals("")) {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.transaction_price) + getString(R.string.yuan));
			} else {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.offer_price) + getString(R.string.yuan));
			}
			if(isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setText(getString(R.string.evaluate_now));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(D1_OrderDetailActivity.this, OrderStatusPayConformed, 0).show();
                        Intent it = new Intent(D1_OrderActivity.this, D3_OrderCommentActivity.class);
                        it.putExtra(O2OMobileAppConst.ORDERINFO, order);
                        startActivityForResult(it, COMMENT_SEND);
                    }
                });
			} else if(isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				mOrderBtnCancel.setText(getString(R.string.evaluate_now));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(D1_OrderActivity.this, D3_OrderCommentActivity.class);
                        it.putExtra(O2OMobileAppConst.ORDERINFO, order);
                        startActivityForResult(it, COMMENT_SEND);
                    }
                });
			} else { // 游客
				setVisitor();
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_EMPLOYEE_COMMENTED.value()) {
			// 雇员已评价
			mOrderWaitView.setVisibility(View.GONE);
			mOrderBtnCancel.setEnabled(true);
			mOrderBtnCancel.setBackgroundResource(R.drawable.c1_apply_btn_select);
			mOrderTransactionPriceView.setVisibility(View.VISIBLE);
			if(order.transaction_price != null && !order.transaction_price.equals("")) {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.transaction_price) + getString(R.string.yuan));
			} else {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.offer_price) + getString(R.string.yuan));
			}
			mOrderCommentView.setVisibility(View.VISIBLE);
			mOrderCommentView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent it = new Intent(D1_OrderActivity.this, D4_OrderCommentListActivity.class);
                    it.putExtra(O2OMobileAppConst.ORDERINFO, order);
                    startActivity(it);
                }
            });
			if(isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setText(getString(R.string.evaluate_now));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent it = new Intent(D1_OrderActivity.this, D3_OrderCommentActivity.class);
                        it.putExtra(O2OMobileAppConst.ORDERINFO, order);
                        startActivityForResult(it, COMMENT_SEND);
                    }
                });
			} else if(isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				mOrderBtnCancel.setEnabled(false);
				mOrderBtnCancel.setBackgroundResource(R.drawable.c1_applied_btn);
				mOrderBtnCancel.setText(getString(R.string.wait_for_evaluate));
			} else { // 游客
				setVisitor();
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_EMPLOYER_COMMENTED.value()) {
			// 雇主已评价
			mOrderWaitView.setVisibility(View.GONE);
			mOrderBtnCancel.setEnabled(true);
			mOrderBtnCancel.setBackgroundResource(R.drawable.c1_apply_btn_select);
			mOrderTransactionPriceView.setVisibility(View.VISIBLE);
			if(order.transaction_price != null && !order.transaction_price.equals("")) {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.transaction_price) + getString(R.string.yuan));
			} else {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.offer_price) + getString(R.string.yuan));
			}
			mOrderCommentView.setVisibility(View.VISIBLE);
			mOrderCommentView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent it = new Intent(D1_OrderActivity.this, D4_OrderCommentListActivity.class);
                    it.putExtra(O2OMobileAppConst.ORDERINFO, order);
                    startActivity(it);
                }
            });
			if(isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setEnabled(false);
				mOrderBtnCancel.setBackgroundResource(R.drawable.c1_applied_btn);
				mOrderBtnCancel.setText(getString(R.string.wait_for_evaluate));
			} else if(isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				mOrderBtnCancel.setText(getString(R.string.evaluate_now));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(D1_OrderActivity.this, D3_OrderCommentActivity.class);
                        it.putExtra(O2OMobileAppConst.ORDERINFO, order);
                        startActivityForResult(it, COMMENT_SEND);
                    }
                });
			} else { // 游客
				setVisitor();
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_FINISHED.value()) {
			// 订单结束
			mOrderWaitView.setVisibility(View.GONE);
			mOrderBtnCancel.setEnabled(true);
			mOrderBtnCancel.setBackgroundResource(R.drawable.c1_apply_btn_select);
			mOrderTransactionPriceView.setVisibility(View.VISIBLE);
			if(order.transaction_price != null && !order.transaction_price.equals("")) {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.transaction_price) + getString(R.string.yuan));
			} else {
				mOrderTransactionPrice.setText(Utils.formatBalance(order.offer_price) + getString(R.string.yuan));
			}
			mOrderCommentView.setVisibility(View.VISIBLE);
			mOrderCommentView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent it = new Intent(D1_OrderActivity.this, D4_OrderCommentListActivity.class);
                    it.putExtra(O2OMobileAppConst.ORDERINFO, order);
                    startActivity(it);
                }
            });
			if(isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setText(getString(R.string.share_evalute));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(D1_OrderActivity.this, D3_OrderCommentCompleteActivity.class);
                        intent.putExtra("order_id", mOrderId);
                        startActivity(intent);
                    }
                });
			} else if(isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				mOrderBtnCancel.setText(getString(R.string.share_evalute));
				mOrderBtnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(D1_OrderActivity.this, D3_OrderCommentCompleteActivity.class);
                        intent.putExtra("order_id", mOrderId);
                        startActivity(intent);
                    }
                });
			} else { // 游客
				setVisitor();
			}
		} else if(order.order_status == ENUM_ORDER_STATUS.OS_CANCELED.value()) {
			// 订单取消
			mOrderWaitView.setVisibility(View.GONE);
			if (isEmp(order.employer)) { // 雇主
				if(order.employee.id != 0) {
					setEmployee(order.employee, false,order);
				}
				mOrderBtnCancel.setEnabled(false);
				mOrderBtnCancel.setBackgroundResource(R.drawable.c1_applied_btn);
				mOrderBtnCancel.setText(getString(R.string.order_have_canceled));
			} else if (isEmp(order.employee)) { // 雇员
				setEmployee(order.employee, true,order);
				mOrderBtnOk.setVisibility(View.GONE);
				mOrderVisibleButtonView.setVisibility(View.GONE);
				mOrderBtnCancel.setEnabled(false);
				mOrderBtnCancel.setBackgroundResource(R.drawable.c1_applied_btn);
				mOrderBtnCancel.setText(getString(R.string.order_have_canceled));
			} else { // 游客
				setVisitor();
			}
		}
	}
	
	/**
	 * 订单详情
	 * @param order
	 */
	private void setOrderDate(final ORDER_INFO order) {
		if(order.employer != null && order.employer.avatar != null && order.employer.avatar.thumb != null) {
			mImageLoader.displayImage(order.employer.avatar.thumb, mOrderEmployerAvatar, O2OMobile.options_head);
		}
		mOrderEmployerAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(D1_OrderActivity.this, F0_ProfileActivity.class);
                intent.putExtra(F0_ProfileActivity.USER_ID, order.employer.id);
                startActivity(intent);
            }
        });
		if(order.employer != null && order.employer.nickname != null) {
			mOrderEmployerName.setText(order.employer.nickname);
		}
		if(order.created_at != null) {
			mOrderTime.setText(TimeUtil.timeAgo(order.created_at));
		}
		if(order.order_sn != null) {
			mOrderSn.setText(order.order_sn);
		}
		if(order.service_type != null && order.service_type.title != null) {
			mOrderServiceType.setText(order.service_type.title);
		}
		if(order.appointment_time != null) {
			mOrderAppointmentTime.setText(order.appointment_time);
		}
		if(order.location != null && order.location.name != null) { 
			mOrderLocation.setText(order.location.name);
		}
		if(order.offer_price != null) {
			mOrderPrice.setText(Utils.formatBalance(order.offer_price) + getString(R.string.yuan));
		}
		if(order.content != null && order.content.text != null && !order.content.text.equals("")) {
			mOrderContentText.setText(order.content.text);
		}
		if(order.content != null && order.content.voice != null) {
			if(!order.content.voice.equals("")) {
				mOrderContentVoiceView.setVisibility(View.VISIBLE);
				mOrderContentVoiceTime.setText(order.duration + "'");
				
				LayoutParams layoutParams_icon = (LayoutParams) mOrderContentVoiceView.getLayoutParams();
				layoutParams_icon.width = mWidthPixels / 6 + order.duration * 3;
				layoutParams_icon.height = mWidthPixels / 12;
				mOrderContentVoiceView.setLayoutParams(layoutParams_icon);
				
			}
			mOrderContentVoiceView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (mPlayer == null) {
                        mOrderContentVoiceIcon.setImageResource(R.anim.voice_play_animation);
                        mAnimationDrawable = (AnimationDrawable) mOrderContentVoiceIcon.getDrawable();
                        mAnimationDrawable.setOneShot(false);
                        mAnimationDrawable.start();

                        Message msg = new Message();
                        msg.what = PLAY_VOICE;
                        msg.obj = order;
                        handler.sendMessage(msg);
                    } else {
                        mPlayer.release();
                        mPlayer = null;
                        mAnimationDrawable.stop();
                        mOrderContentVoiceIcon.setImageResource(R.drawable.d8_btn_playing_2);
                    }
                }
            });
		}
		mOrderStatus.setText(StringUtils.getOrderStatusName(order.order_status));
	}
	
	/**
	 * @param employee 雇员
	 * @param isEmployee 如果是雇员则显示雇主电话否则显示雇员电话
	 */
	private void setEmployee(final SIMPLE_USER employee, boolean isEmployee,ORDER_INFO order) {
        if(!"".equals(order.accept_time)){
            mAcceptOrderTime.setText(TimeUtil.timeAgo(order.accept_time));
        }
		if(employee != null && employee.avatar != null && employee.avatar.thumb != null) {
			mImageLoader.displayImage(employee.avatar.thumb, mOrderEmployeeAvatar, O2OMobile.options_head);
		}
		mOrderEmployeeAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(D1_OrderActivity.this, F0_ProfileActivity.class);
                intent.putExtra(F0_ProfileActivity.USER_ID, employee.id);
                startActivity(intent);
            }
        });
		if(employee.nickname != null) {
			mOrderEmployeeName.setText(employee.nickname);
		}
		if(isEmployee) {
			mOrderEmployerPhone.setVisibility(View.VISIBLE);
			mOrderEmployerPhone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mOrderInfoModel.publicOrder.employer.mobile_phone));
                    startActivity(intent);
                }
            });
		} else {
			mOrderEmployeePhone.setVisibility(View.VISIBLE);
			mOrderEmployeePhone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + employee.mobile_phone));
                    startActivity(intent);
                }
            });
		}
	}
	
	/**
	 * 游客模式，不显示订单状态
	 */
	private void setVisitor() {

        mOrderEmployeeView.setVisibility(View.GONE);
        mOrderStatusArrow.setVisibility(View.GONE);
		mOrderStatusView.setEnabled(false);
		mOrderBtnCancel.setVisibility(View.GONE);
		mOrderStatusView.setBackgroundColor(Color.parseColor("#eeeeee"));
		mOrderStatus.setText(getString(R.string.order_have_been_received));

	}
	
	/**
	 * 判断是否是雇主/雇员
	 */
	public boolean isEmp(SIMPLE_USER user) {
		if(user.id == SESSION.getInstance().uid) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		mOrderInfoModel.get(mOrderId);
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
			mAnimationDrawable.stop();
			mOrderContentVoiceIcon.setImageResource(R.drawable.d8_btn_playing_2);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == COMMENT_SEND) {
			if (data != null) {
				ORDER_INFO order = (ORDER_INFO) data.getSerializableExtra(O2OMobileAppConst.ORDERINFO);
				setOrderDetailView(order);
			}
		}
        else
        {
            //Todo
        }
	}


    public void hidePayView()
    {
        AnimationUtil.backAnimationFromBottom(mOrderPlayOrderView);
        mOrderPlayOrderView.setVisibility(View.GONE);
        AnimationUtil.backAnimation(mOrderPlayButtonView);
        mOrderPlayButtonView.setVisibility(View.GONE);
        Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mOrderPlay.setVisibility(View.GONE);
                mOrderBtnCancel.setVisibility(View.VISIBLE);
            }
        };
        mHandler.sendEmptyMessageDelayed(0, 200);
    }
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == PLAY_VOICE) {
				ORDER_INFO order = (ORDER_INFO) msg.obj;
				mPlayer = new MediaPlayer();
				try {
					mPlayer.setDataSource(order.content.voice);
					mPlayer.prepare();
    				mPlayer.start();
    				mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            mPlayer = null;
                            mAnimationDrawable.stop();
                            mOrderContentVoiceIcon.setImageResource(R.drawable.d8_btn_playing_2);
                        }
                    });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

    @Override
    protected void onNewIntent(Intent intent) {
        // 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        mOrderId = intent.getIntExtra(ORDER_ID, 0);
        mOrderInfoModel.get(mOrderId);
    }
	
}
