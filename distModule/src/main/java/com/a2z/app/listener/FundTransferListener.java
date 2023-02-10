package com.a2z.app.listener;

import com.a2z.app.model.FundTransfer;

public interface FundTransferListener {
    void onFundTransfer(FundTransfer fundTransfer,int type);
}
