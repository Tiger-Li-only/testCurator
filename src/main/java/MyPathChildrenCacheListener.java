import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * Created by Administrator on 2017-12-14 0014.
 */
public class MyPathChildrenCacheListener implements PathChildrenCacheListener {
    @Override
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
        switch (event.getType()){
            case CHILD_ADDED:
                System.out.println("CHILD_ADDED:"+event.getData());
                break;
            case CHILD_UPDATED:
                System.out.println("CHILD_UPDATED:"+event.getData());
                break;
            case CHILD_REMOVED:
                System.out.println("CHILD_REMOVED:"+event.getData());
                break;
            default:
                break;
        }
    }
}
