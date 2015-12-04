package com.nfortics.mfinanceV2.Request;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Merchant;

/**
 * Created by bigfire on 11/19/2015.
 */
public class CollectionLimitResetRequest implements Request {

    public static final String BASE_URL = Application.serverURL2
            + "agents/reset_limit.json?";
    private static String url;
    Merchant merchant =Merchant.getActiveMerchant("true");

    public CollectionLimitResetRequest() {

        buildUrl();
    }

    public void buildUrl() {
        StringBuffer urlBuilder = new StringBuffer(BASE_URL);
        addAgentMsisdn(urlBuilder);
        addAccountCode(urlBuilder);

        url = urlBuilder.toString();
    }

    private void addAgentMsisdn(StringBuffer urlBuilder) {
        urlBuilder.append("agent_msisdn=");
        urlBuilder.append(Application.getActiveAgent().getMsisdn());
    }

    private void addAccountCode(StringBuffer urlBuilder) {
        urlBuilder.append("&account_code=");
        urlBuilder.append(merchant.getCode());
    }

    @Override
    public String getURL() {
        // TODO Auto-generated method stub
        return url;
    }

}
