import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017-12-14 0014.
 */
public class CreateSession {
    public static void main(String args[]) throws Exception {
        //时间间隔越来越长的重试策略，例：起始时间1秒，重试3次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        //最大重试次数策略,例：重连五次，五次间隔1秒
        RetryPolicy retryPolicy1 = new RetryNTimes(5,1000);

        //一直重连，直到达到规定的时间,例：一共十秒，每次间隔1秒
        RetryPolicy retryPolicy2 = new RetryUntilElapsed(10000,1000);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.100.128:2181",5000,5000,retryPolicy);
        client.start();  //启动

        CuratorFramework client2 = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.100.128:2181")  //请求url
                .sessionTimeoutMs(5000) //会话超时时间
                .connectionTimeoutMs(5000)  //连接超时时间
                .retryPolicy(retryPolicy1)  //重连策略
//                .authorization("权限列表") //权限相关
                .build();
        client2.start();
        //创建节点
        String path = client.create()
                .creatingParentsIfNeeded() //创建节点的父节点不存在也能创建成功
                .withMode(CreateMode.EPHEMERAL)
//                .withACL(aclList)  //权限列表
                .forPath("/node_curator1/curator_1","123".getBytes());
        System.out.println(path);

        //删除节点
        //删除不含子节点的节点，保障删除成功
        client.delete().guaranteed().forPath("/node_curator1");
        System.out.println("删除节点：/node_curator1");

        //删除含子节点的节点
        client.delete().deletingChildrenIfNeeded().withVersion(-1).forPath("/node_curator1");
//
        //获取子节点
        List<String> childrenList = client.getChildren().forPath("/node_1");
        System.out.println("/node_1下的子节点:"+childrenList);
        //查询节点数据
        String data = new String(client.getData().forPath("/node_1")) ;
        System.out.println("/node_1节点数据:"+data);
//
//        //获取状态信息
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/node_1");
        System.out.println("/node_1节点状态:"+stat);
////
//        //更改节点的值,校验版本
        client.setData().withVersion(stat.getVersion()).forPath("/node_1", "456".getBytes());
        System.out.println("/node_1的节点值:"+new String(client.getData().forPath("/node_1")));
//
        //检测节点是否可用,如果节点不存在，返回null
        Stat stat1 = client.checkExists().forPath("/node_1");
        System.out.println("/node_1节点是否存在,为null说明不存在"+stat1);
//
//
        //异步操作，例如
        //引入线程池
        ExecutorService es = Executors.newFixedThreadPool(5);

        Stat stat2 = client.checkExists().inBackground(new MyBackgroundCallback(),"上下文对象",es).forPath("/node_1");

       //事件监听，数据变化
       NodeCache cache = new NodeCache(client,"/node_1");
       MyNodeCacheListener cacheListener = new MyNodeCacheListener();
       cacheListener.setCache(cache);
       cache.start();
       cache.getListenable().addListener(cacheListener);


        //事件监听，子节点改变
        PathChildrenCache childrenCache = new PathChildrenCache(client,"/node_1",true);
        childrenCache.start();
        childrenCache.getListenable().addListener(new MyPathChildrenCacheListener(),es);
        Thread.sleep(Integer.MAX_VALUE);
//


    }
}
