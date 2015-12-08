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
package org.meta.model.files;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.meta.api.model.DataFile;

/**
 * Just static accessor for data file read/write operation.
 *
 * Provides a bit of caching to avoid opening too many files unnecessarily.
 *
 * TODO remove this and instead think of a way to store calcutated info about a data file (pieces, hash, ...)
 * in the storage layer.
 *
 * @author dyslesiq
 */
public final class DataFileAccessors {

    private final static int EXECUTOR_THREADS = 1;

    private final static ExecutorService filesExecutor = Executors.newFixedThreadPool(EXECUTOR_THREADS);

    private static Map<URI, DataFileAccessor> accessors = new ConcurrentHashMap<>();

    private DataFileAccessors() {
    }

    /**
     * Get a read/write accessor for the given dataFile.
     *
     * @param dataFile the DataFile
     * @return the accessor for the given datafile
     */
    public static synchronized DataFileAccessor getAccessor(final DataFile dataFile) {
        if (!accessors.containsKey(dataFile.getURI())) {
            accessors.put(dataFile.getURI(), new DataFileAccessor(dataFile));
        }
        return accessors.get(dataFile.getURI());
    }

    /**
     *
     * @return the files executor service
     */
    public static ExecutorService getFilesExecutor() {
        return filesExecutor;
    }

}
