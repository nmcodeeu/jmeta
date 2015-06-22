/*
 *	JMeta - Meta's java implementation
 *	Copyright (C) 2013 Nicolas Michon
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the
 *	License, or (at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Affero General Public License for more details.
 *	
 *	You should have received a copy of the GNU Affero General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.meta.dht;

import java.io.IOException;
import org.meta.common.MetHash;
import org.meta.dht.tomp2p.TomP2pDHT;

/**
 *
 * @author nico
 */
public abstract class MetaDHT {

    public static MetaDHT getInstance() {
        return new TomP2pDHT();
    }

    /**
     * Initializes the DHT with the given configuration.
     * 
     * Registers our identity and starts listening for peers in the DHT.
     * 
     * @param configuration The {@link DHTConfiguration} holding configuration.
     * 
     * @throws java.io.IOException If an underlying network operation failed.
     */
    public abstract void start(DHTConfiguration configuration) throws IOException;

    /**
     * Bootstrap the DHT using broadcast to find nodes.
     * 
     * @return The asynchronous BootstrapOperation representing the outcome
     * of the bootstrap.
     */
    public abstract BootstrapOperation bootstrap();

    /**
     * @param hash. The hash to find peers for.
     * 
     * @return The asynchronous FindPeersOperation representing the outcome 
     * of the operation.
     */
    public abstract FindPeersOperation findPeers(MetHash hash);

    /**
     * 
     * @param hash The hash to store on the DHT.
     * @return The asynchronous StoreOperation representing the outcome of 
     * the operation.
     */
    public abstract StoreOperation store(MetHash hash);

}