/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef LIBHDFS_HDFS_CLASSES_H
#define LIBHDFS_HDFS_CLASSES_H

#include <jni.h>

/**
 * A collection of commonly used jclass objects that are used throughout
 * hdfs.c. The jclasses are loaded immediately after the JVM is created (see
 * jni_helper.h getJNIEnv). By pre-allocated the jclass objects we avoid
 * duplicate calls to FindClass every time we want to invoke a Java method via
 * the JNI.
 *
 * jni_helper.c provides similar functionality by storing all created
 * jclass instances into a htable, however, the htable is protected by a
 * single mutex which can lead to high lock contention.
 *
 * Only a subset of the  jclasses used by libhdfs are allocated here in order
 * to avoid allocating space for jclass objects that are not needed. Instead,
 * only the most commonly used jclass objects are pre-allocated / only the
 * jclass objects  that are on the "hot-path" (e.g. any libhdfs API that
 * reads / writes data).
 */

extern jclass JC_Configuration;
extern jclass JC_Path;
extern jclass JC_FileSystem;
extern jclass JC_FSDataInputStream;
extern jclass JC_FSDataOutputStream;
extern jclass JC_FileStatus;
extern jclass JC_FsPermission;
extern jclass JC_ReadStatistics;
extern jclass JC_HdfsDataInputStream;
extern jclass JC_ByteBuffer;

/* Some frequently used HDFS paths */
#define HADOOP_CONF     "org/apache/hadoop/conf/Configuration"
#define HADOOP_PATH     "org/apache/hadoop/fs/Path"
#define HADOOP_LOCALFS  "org/apache/hadoop/fs/LocalFileSystem"
#define HADOOP_FS       "org/apache/hadoop/fs/FileSystem"
#define HADOOP_FSSTATUS "org/apache/hadoop/fs/FsStatus"
#define HADOOP_FILEUTIL "org/apache/hadoop/fs/FileUtil"
#define HADOOP_BLK_LOC  "org/apache/hadoop/fs/BlockLocation"
#define HADOOP_DFS      "org/apache/hadoop/hdfs/DistributedFileSystem"
#define HADOOP_FSDISTRM "org/apache/hadoop/fs/FSDataInputStream"
#define HADOOP_FSDOSTRM "org/apache/hadoop/fs/FSDataOutputStream"
#define HADOOP_FILESTAT "org/apache/hadoop/fs/FileStatus"
#define HADOOP_FSPERM   "org/apache/hadoop/fs/permission/FsPermission"
#define HADOOP_RSTAT    "org/apache/hadoop/hdfs/ReadStatistics"
#define HADOOP_HDISTRM  "org/apache/hadoop/hdfs/client/HdfsDataInputStream"
#define READ_OPTION     "org/apache/hadoop/fs/ReadOption"

/* Some frequently used Java paths */
#define JAVA_NET_ISA    "java/net/InetSocketAddress"
#define JAVA_NET_URI    "java/net/URI"
#define JAVA_BYTEBUFFER "java/nio/ByteBuffer"
#define JAVA_STRING     "java/lang/String"

#endif /*LIBHDFS_HDFS_CLASSES_H*/
