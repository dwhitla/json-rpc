/*******************************************************************************
 * Copyright (c) 2000-2016, Flight Centre Limited. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR OTHER CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 ******************************************************************************/

package net.flitech.jsonrpc;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


@TransactionManagement
@Stateless
@LocalBean
public class JSONRPCDispatcherServiceBean implements JSONRPCDispatcherService {

    public JSONRPCDispatcherServiceBean() {
    }

    @Override
    public <S, R> R call(Class<S> service, String method, Object... params) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        serialize(stream);
        deserialize(new ByteArrayInputStream(stream.toByteArray()));
        return null;
    }

    private void deserialize(InputStream stream) {
    }

    private void serialize(OutputStream stream) {
    }

}
