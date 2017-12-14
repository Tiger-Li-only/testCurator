import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;

import java.util.List;

/**
 * Created by Administrator on 2017-12-14 0014.
 */
public class MyBackgroundCallback implements BackgroundCallback {
    @Override
    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
        CuratorEventType t = curatorEvent.getType();  //枚举：CREATE, DELETE, EXISTS, GET_DATA, SET_DATA, CHILDREN, SYNC, GET_ACL, SET_ACL, TRANSACTION, GET_CONFIG, RECONFIG, WATCHED, REMOVE_WATCHES, CLOSING;
        int r = curatorEvent.getResultCode();  //返回码
        Object o = curatorEvent.getContext();  //获取上下文对象
        String path = curatorEvent.getPath();  //路径
        List<String> cList = curatorEvent.getChildren();  //子节点列表
        String data = new String(curatorEvent.getData());  //节点数据

    }
}
