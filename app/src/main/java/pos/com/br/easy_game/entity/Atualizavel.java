package pos.com.br.easy_game.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexandre on 31/08/15.
 */
public interface Atualizavel {

    void atualizar(JSONObject jsonObject) throws JSONException;
}
