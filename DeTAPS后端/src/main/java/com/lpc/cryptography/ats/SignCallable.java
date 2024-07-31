package com.lpc.cryptography.ats;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map.Entry;
import java.util.concurrent.Callable;

public class SignCallable  implements Callable<Entry<Element,Element>>{
    private ATS atsInstance;
    private String skstr;
    private String m;
    private String gid;

    public SignCallable(ATS atsInstance, String skstr, String m, String gid) {
        this.atsInstance=atsInstance;
        this.skstr = skstr;
        this.m = m;
        this.gid = gid;
    }

    @Override
    public Entry<Element,Element> call() {
        Entry<Element, Element> share = atsInstance.Sign(skstr, m, gid);
        return share;
    }

}
