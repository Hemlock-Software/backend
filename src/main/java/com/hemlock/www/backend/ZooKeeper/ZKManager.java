package com.hemlock.www.backend.ZooKeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZKManager {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";//客户端连接地址
    private static final String ROOT_NODE = "/root";//客户端根节点
    private static final String ROOT_NODE_CHILDREN = "/root/children";//客户端子节点
    private static final int SESSION_TIMEOUT = 5000;//会话超时时间
    private static final int CONNECTION_TIMEOUT = 10000;//连接超时时间
    private static CuratorFramework ZKclient = null;//创建zookeeper连接实例

    /**
     * 重试策略
     * baseSleepTimeMs：初始的重试等待时间，单位毫秒
     * maxRetries：最多重试次数
     */
    private static final RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    /**
     * 重试策略
     * n：最多重试次数
     * sleepMsBetweenRetries：重试时间间隔，单位毫秒
     */
    private static final RetryPolicy retry = new RetryNTimes(3, 2000);

    public ZKManager() throws Exception {
        // 创建Curator连接对象
        connectCuratorClient();
        test();
    }

    private void test() throws Exception {
        watchClusterMembers();
        createNode("test1", "hello");
        createNode("test2", "hello");
        deleteNode("test1");
        updateNode("test2","hi");
    }

    private static void connectCuratorClient() {

        //创建zookeeper连接
        ZKclient = CuratorFrameworkFactory.builder().
                connectString(ZK_ADDRESS).
                sessionTimeoutMs(SESSION_TIMEOUT).
                connectionTimeoutMs(CONNECTION_TIMEOUT).
                retryPolicy(retry).
                build();
        //启动客户端
        ZKclient.start();
        System.out.println("zookeeper初始化连接成功：" + ZKclient);
    }

    public static void createNode(String nodePath, String data) throws Exception {
        if (nodePath == null) {
            System.out.println("节点不能为空");
            return;
        }
        String path = ROOT_NODE + "/" + nodePath;
        Stat exists = ZKclient.checkExists().forPath(path);
        if (null != exists) {
            System.out.println("节点[" + path + "]已存在");
            return;
        }
        if (null != data) {
            ZKclient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
            System.out.println("节点[" + path + "]创建成功");
        }
    }

    //监听节点变化
    public void watchClusterMembers() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(ZKclient, ROOT_NODE, true);
        pathChildrenCache.getListenable().addListener((client, event) -> {
            if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED
                    || event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                System.out.print("节点发生变化, 当前节点如下: ");
                for (String member : ZKclient.getChildren().forPath(ROOT_NODE)) {
                    System.out.print(member+" ");
                }
                System.out.println();
            }
        });
        pathChildrenCache.start();
    }

    private static void deleteNode(String nodePath) throws Exception {
        if (nodePath == null) {
            System.out.println("节点不能为空");
            return;
        }
        String path = ROOT_NODE + "/" + nodePath;
        ZKclient.delete().guaranteed().forPath(path);
        System.out.println("节点[" + path + "]删除成功");
    }

    private static void updateNode(String nodePath, String data) throws Exception {
        if (nodePath == null) {
            System.out.println("节点不能为空");
            return;
        }
        String path = ROOT_NODE + "/" + nodePath;
        Stat stat = ZKclient.setData().inBackground().forPath(path, data.getBytes());
        if (null != stat) {
            System.out.println("节点[" + path + "]更新成功");
        }
    }
}
