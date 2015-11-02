/*
 *
 * JMeta - Meta's java implementation
 *
 * Copyright (C) 2013-2015 Pablo Joubert
 * Copyright (C) 2013-2015 Thomas Lavocat
 * Copyright (C) 2013-2015 Nicolas Michon
 *
 * This file is part of JMeta.
 *
 * JMeta is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * JMeta is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.meta.p2pp.client.requests;

import java.nio.ByteBuffer;
import org.meta.api.common.MetHash;
import org.meta.p2pp.P2PPConstants;
import org.meta.p2pp.P2PPConstants.ClientRequestStatus;
import org.meta.p2pp.client.P2PPRequest;
import org.meta.p2pp.client.P2PPResponseHandler;

/**
 *
 * The response handler for the Get request.
 *
 * @author dyslesiq
 */
public class P2PPGetResponseHandler extends P2PPResponseHandler {

    private MetHash pieceHash;

    private ByteBuffer data;

    private final int requestedLength;

    /**
     *
     * @param req the request
     * @param dataLength the request data length
     */
    public P2PPGetResponseHandler(final P2PPRequest req, final int dataLength) {
        super(req);
        this.requestedLength = dataLength;
    }

    @Override
    public P2PPConstants.ClientRequestStatus dataReceived() {
        ClientRequestStatus status = this.request.getStatus();

//        if (status == ClientRequestStatus.RESPONSE_HEADER_PENDING) {
//            if (!this.buffers[0].hasRemaining()) {
//                status = ClientRequestStatus.RESPONSE_HEADER_RECEIVED;
//                if (!this.parseHeader()) {
//                    status = ClientRequestStatus.INVALID;
//                } else {
//                    this.buffersOffset = 1;
//                    this.buffersLength = 1;
//                    this.buffers[this.buffersOffset] = ByteBuffer.allocateDirect(payloadSize);
//                }
//            }
//        } else if (status == ClientRequestStatus.RESPONSE_PENDING) {
//            if (!this.buffers[this.buffersOffset].hasRemaining()) {
//                status = ClientRequestStatus.RESPONSE_RECEIVED;
//            }
//        }
        return status;
    }

    @Override
    public boolean parse() {
        ByteBuffer buf = this.payloadBuffer;
        buf.rewind();
        short sizeofHash = buf.getShort();
        pieceHash = new MetHash(buf, sizeofHash);
        this.data = ByteBuffer.allocate(requestedLength);
        data.put(buf);
        return true;
    }

    /**
     *
     * @return the fetched data
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     *
     * @return the piece hash
     */
    public MetHash getPieceHash() {
        return pieceHash;
    }

}
