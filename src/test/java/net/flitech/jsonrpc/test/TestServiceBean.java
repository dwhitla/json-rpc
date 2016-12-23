package net.flitech.jsonrpc.test;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;


@LocalBean
@Stateless
public class TestServiceBean implements TestService {
    public Z testMethod(X x, Y y) {
        return new Z(x, y);
    }
}
