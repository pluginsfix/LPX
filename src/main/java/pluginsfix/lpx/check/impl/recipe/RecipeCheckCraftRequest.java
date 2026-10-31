package pluginsfix.lpx.check.impl.recipe;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractRecipeCheckT;
import pluginsfix.lpx.check.manager.CheckManagerImpl;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCraftRecipeRequest;
public class RecipeCheckCraftRequest extends AbstractRecipeCheckT<WrapperPlayClientCraftRecipeRequest> {
    final CheckManagerImpl this$0;
    public RecipeCheckCraftRequest(CheckManagerImpl owner, ProtocolPlayer player, CheckOptions options) {
        super(player, options);
        this.this$0 = owner;
    }
    @Override
    public int F6(Object[] args) {
        WrapperPlayClientCraftRecipeRequest packet = (WrapperPlayClientCraftRecipeRequest) args[0];
        return packet.getRecipeId().getId();
    }
    public int Uh(Object[] args) {
        return F6(args);
    }
}
