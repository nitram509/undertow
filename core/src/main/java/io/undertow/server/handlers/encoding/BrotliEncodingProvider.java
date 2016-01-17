/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.undertow.server.handlers.encoding;

import io.undertow.conduits.BrotliStreamSinkConduit;
import io.undertow.server.ConduitWrapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.ConduitFactory;
import org.scijava.nativelib.NativeLoader;
import org.xnio.conduits.StreamSinkConduit;

import java.io.IOException;

/**
 * Content coding for 'brotli'
 *
 * @author Martin W. Kirst
 */
public class BrotliEncodingProvider implements ContentEncodingProvider {

    static {
        try {
            NativeLoader.loadLibrary("brotli");
        } catch (IOException e) {
            throw new RuntimeException(BrotliStreamSinkConduit.class.getName(), e);
        }
    }

    @Override
    public ConduitWrapper<StreamSinkConduit> getResponseWrapper() {
        return new ConduitWrapper<StreamSinkConduit>() {
            @Override
            public StreamSinkConduit wrap(ConduitFactory<StreamSinkConduit> factory, HttpServerExchange exchange) {
                return new BrotliStreamSinkConduit(factory, exchange);
            }
        };
    }
}