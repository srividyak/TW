package com.tapwisdom.service;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import java.io.IOException;

/**
 * Created by srividyak on 02/07/15.
 */
public class InMemoryMongoDB {

    private MongodExecutable mongodExecutable;
    private String server;
    private int port;

    public InMemoryMongoDB(String server, String port) {
        this.server = server;
        this.port = Integer.parseInt(port);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig config = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build();
        mongodExecutable = starter.prepare(config);
        mongodExecutable.start();
    }

    public void destroy() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }
}
