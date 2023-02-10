package com.di_md.a2z.listener;

import com.di_md.a2z.model.FundTransfer;

public interface FundTransferListener {
    void onFundTransfer(FundTransfer fundTransfer,int type);
}
