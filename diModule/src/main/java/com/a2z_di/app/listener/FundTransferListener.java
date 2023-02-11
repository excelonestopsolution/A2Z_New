package com.a2z_di.app.listener;

import com.a2z_di.app.model.FundTransfer;

public interface FundTransferListener {
    void onFundTransfer(FundTransfer fundTransfer,int type);
}
