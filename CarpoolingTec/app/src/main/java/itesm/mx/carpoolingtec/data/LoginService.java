package itesm.mx.carpoolingtec.data;

import android.util.Base64;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import itesm.mx.carpoolingtec.model.Alumno;
import itesm.mx.carpoolingtec.util.UnauthorizedException;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class LoginService {

    private static final String LOGIN_URL = "https://alsvdbw01.itesm.mx/autentica/servicio/identidad";

    public Single<Alumno> login(String username, String password) {

        final String header = username + ":" + password;

        return Single.create(new SingleOnSubscribe<Alumno>() {
            @Override
            public void subscribe(SingleEmitter<Alumno> e) throws Exception {
                HttpsURLConnection httpURLConnection = null;

                try {
                    URL url = new URL(LOGIN_URL);
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");

                    byte[] loginBytes = header.getBytes();
                    StringBuilder loginBuilder = new StringBuilder()
                            .append("Basic ")
                            .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));
                    httpURLConnection.addRequestProperty("Authorization", loginBuilder.toString());

                    int resCode = httpURLConnection.getResponseCode();

                    switch (resCode) {
                        case HTTP_OK:
                            InputStream in = httpURLConnection.getInputStream();
                            String inString = readStream(in);
                            XmlParser xmlParser = new XmlParser();
                            Alumno alumno = xmlParser.parse(inString);
                            if (alumno == null) {
                                throw new Exception();
                            } else {
                                e.onSuccess(alumno);
                            }
                            break;
                        case HTTP_UNAUTHORIZED:
                            throw new UnauthorizedException();
                        default:
                            throw new Exception(httpURLConnection.getResponseMessage());
                    }

                } catch (Exception ex) {
                    e.onError(ex);
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        });
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private static String readStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static class XmlParser {

        public static Alumno parse(String xml) throws XmlPullParserException, IOException {
            Alumno alumno = null;
            boolean persona = false;
            String text = null;
            String nombre = null;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                factory.setNamespaceAware(true);
                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagname = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (tagname.equalsIgnoreCase("alumno")) {
                                alumno = new Alumno();
                            } else if (tagname.equalsIgnoreCase("persona")){
                                persona = true;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if (tagname.equalsIgnoreCase("nombre") && persona) {
                                alumno.setNombre(text);
                            } else if (tagname.equalsIgnoreCase("apellidoPaterno") && persona) {
                                alumno.setApellidoPaterno(text);
                            } else if (tagname.equalsIgnoreCase("apellidoMaterno") && persona) {
                                alumno.setApellidoMaterno(text);
                            } else if (tagname.equalsIgnoreCase("matricula")) {
                                alumno.setMatricula(text);
                            }
                            break;

                        default:
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return alumno;
        }
    }
}
