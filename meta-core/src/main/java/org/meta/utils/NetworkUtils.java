/*
 *    JMeta - Meta's java implementation
 *    Copyright (C) 2013 Nicolas Michon
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.meta.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.meta.api.configuration.NetworkConfiguration;
import org.meta.api.dht.MetaPeer;

/**
 *
 * Utility class for miscellaneous network functions.
 *
 * @author nico
 */
public class NetworkUtils {

    /**
     * Get the loopback interface name.
     *
     * @return the loopback interface name.
     */
    public static String getLoopbackInterface() {
        try {
            for (NetworkInterface netIf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (netIf == null) {
                    continue;
                }
                if (netIf.isLoopback()) {
                    return netIf.getName();
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    /**
     * @param addr the address to check
     *
     * @return true if the address is publicly routable, false otherwise.
     */
    public static boolean isPublicAddress(InetAddress addr) {
        return !addr.isLinkLocalAddress()
            && !addr.isSiteLocalAddress()
            && !addr.isLoopbackAddress();
    }

    /**
     *
     * @param addr The address to check.
     * @return true of the address is routable within the local network, false
     * otherwise.
     */
    public static boolean isLocalNetworkAddress(InetAddress addr) {
        return addr.isSiteLocalAddress();
    }

    /**
     * Get the first address found for the given interface. The returned address
     * will be stored in the DHT for the local-only mode.
     *
     * @param ifaceName The interface name
     * @param ipV4 true if searching ipV4 address, false if searching ipV6.
     *
     * @return The IntetAddress or null if not found.
     */
    public static InetAddress getInterfaceAddress(String ifaceName, boolean ipV4) {
        try {
            NetworkInterface iface = NetworkInterface.getByName(ifaceName);
            for (InetAddress ifAddr : Collections.list(iface.getInetAddresses())) {
                if (ifAddr instanceof Inet4Address && ipV4) {
                    return ifAddr;
                } else if (ifAddr instanceof Inet6Address && !ipV4) {
                    return ifAddr;
                }
            }
            return null;
        } catch (SocketException ex) {
            return null;
        }
    }

    /**
     * Get the all addresses found for the given interface of the given type.
     *
     * @param ifaceName The interface name
     * @param ipV4 true if searching ipV4 address, false otherwise
     * @param ipV6 true if searching ipV6 address, false otherwise
     *
     * @return The List of addresses. Might be empty.
     */
    public static Collection<InetAddress> getInterfaceAddresses(String ifaceName, boolean ipV4, boolean ipV6) {
        Collection<InetAddress> addrs = new ArrayList<>();
        try {
            NetworkInterface iface = NetworkInterface.getByName(ifaceName);
            for (InetAddress ifAddr : Collections.list(iface.getInetAddresses())) {
                if (ipV4 && ifAddr instanceof Inet4Address) {
                    addrs.add(ifAddr);
                } else if (ipV6 && ifAddr instanceof Inet6Address) {
                    addrs.add(ifAddr);
                }
            }
        } catch (SocketException ex) {
            //Ignore invalid interface or addresses.
        }
        return addrs;
    }

    /**
     * Get all addresses found by reading the network configuration.
     *
     * @param nwConfig The network configuration.
     * @return The list of found addresses.
     */
    public static Collection<InetAddress> getConfigAddresses(NetworkConfiguration nwConfig) {
        Collection<InetAddress> addresses = new ArrayList();

        if (nwConfig.getAddresses() != null) {
            for (InetAddress addr : nwConfig.getAddresses()) {
                if (nwConfig.ipV4() && addr instanceof Inet4Address) {
                    addresses.add(addr);
                } else if (nwConfig.ipV6() && addr instanceof Inet6Address) {
                    addresses.add(addr);
                }
            }
        }
        if (nwConfig.getInterfaces() != null) {
            for (String iface : nwConfig.getInterfaces()) {
                Collection<InetAddress> ifaceAddresses
                    = getInterfaceAddresses(iface, nwConfig.ipV4(), nwConfig.ipV6());
                addresses.addAll(ifaceAddresses);
            }
        }
        return addresses;
    }

    /**
     * Get the subset of peers that have local addresses.
     *
     * @param peers The initial list of peers.
     *
     * @return The list of peers found to have local addresses.
     */
    public static Collection<MetaPeer> getLocalPeers(Collection<MetaPeer> peers) {
        Collection<MetaPeer> localPeers = new ArrayList<>();

        for (MetaPeer peer : peers) {
            if (isLocalNetworkAddress(peer.getAddress()) || peer.getAddress().isLoopbackAddress()) {
                localPeers.add(peer);
            }
        }
        return localPeers;
    }

    /**
     * Get the subset of peers that have public addresses.
     *
     * @param peers The initial list of peers.
     *
     * @return The list of peers found to have public addresses.
     */
    public static Collection<MetaPeer> getPublicPeers(Collection<MetaPeer> peers) {
        Collection<MetaPeer> publicPeers = new ArrayList<>();

        for (MetaPeer peer : peers) {
            if (isPublicAddress(peer.getAddress())) {
                publicPeers.add(peer);
            }
        }
        return publicPeers;
    }

}