import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * Created by Administrator on 2017-12-14 0014.
 */
public class MyNodeCacheListener implements NodeCacheListener {
    private NodeCache cache;
    @Override
    public void nodeChanged() throws Exception {
        String data = new String(cache.getCurrentData().getData());
        System.out.println("节点的值变成了："+data);

    }

    public void setCache(NodeCache cache){
        this.cache = cache;
    }

}
