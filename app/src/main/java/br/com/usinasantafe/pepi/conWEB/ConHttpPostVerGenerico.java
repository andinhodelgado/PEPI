package br.com.usinasantafe.pepi.conWEB;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import br.com.usinasantafe.pepi.bo.ManipDadosVerif;

/**
 * Created by anderson on 16/11/2015.
 */
public class ConHttpPostVerGenerico extends AsyncTask<String, Void, String> {

    private static ConHttpPostCadGenerico instance = null;
    private Map<String, Object> parametrosPost = null;
    private ManipDadosVerif manipDadosVerif;
    private String tipo;

    public ConHttpPostVerGenerico() {
    }

    @Override
    protected String doInBackground(String... arg) {

        BufferedReader bufferedReader = null;
        String resultado = null;

        String url = arg[0];

        try {

            Log.i("ERRO", "Chegou aki1");
            String parametros = getQueryString(parametrosPost);
            URL urlCon = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlCon.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            OutputStream out = connection.getOutputStream();
            byte[] bytes = parametros.getBytes("UTF8");
            out.write(bytes);
            out.flush();
            out.close();

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer("");
            String line = "";
            String LS = System.getProperty("line.separator");
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + LS);
            }
            bufferedReader.close();
            resultado = stringBuffer.toString();

            connection.disconnect();

        } catch (Exception e) {
            Log.i("ERRO", "Erro = " + e);
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (Exception er) {
                    Log.i("ERRO", "Erro = " + er);
                }

            }
        }
        finally{

            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    Log.i("ERRO", "Erro = " + e);
                }

            }

        }
        return resultado;
    }

    protected void onPostExecute(String result) {

        try {
            Log.i("pepi", "VALOR RECEBIDO --> " + result);
            ManipDadosVerif.getInstance().manipularDadosHttp(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("ERRO", "Erro2 = " + e);
        }

    }

    public void setParametrosPost(Map<String, Object> parametrosPost) {
        this.parametrosPost = parametrosPost;
    }

    private String getQueryString(Map<String, Object> params) throws Exception {
        if (params == null || params.size() == 0) {
            return null;
        }
        String urlParams = null;
        Iterator<String> e = (Iterator<String>) params.keySet().iterator();
        while (e.hasNext()) {
            String chave = (String) e.next();
            Object objValor = params.get(chave);
            String valor = objValor.toString();
            urlParams = urlParams == null ? "" : urlParams + "&";
            urlParams += chave + "=" + valor;
        }
        return urlParams;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
