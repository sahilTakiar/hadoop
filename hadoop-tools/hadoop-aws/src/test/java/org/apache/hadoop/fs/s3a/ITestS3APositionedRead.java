/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.fs.s3a;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.apache.hadoop.fs.contract.ContractTestUtils.createFile;
import static org.apache.hadoop.fs.contract.ContractTestUtils.dataset;

public class ITestS3APositionedRead extends AbstractS3ATestBase {

  @Test
  public void testPositionedRead() throws IOException {
    Path file = path("testPositionedRead");
    FileSystem fs = getFileSystem();
    byte[] dataset = dataset(TEST_FILE_LEN, 0, 255);
    createFile(fs, file, true, dataset);

    // Read entire file
    FSDataInputStream stream = getFileSystem().open(file);
    byte[] output1 = new byte[TEST_FILE_LEN];
    assertEquals(TEST_FILE_LEN, stream.read(0, output1, 0, TEST_FILE_LEN));
    assertArrayEquals(dataset, output1);
    stream.close();

    // Read first half of the file
    stream = getFileSystem().open(file);
    byte[] output2 = new byte[TEST_FILE_LEN / 2];
    assertEquals(TEST_FILE_LEN / 2, stream.read(0, output2, 0, TEST_FILE_LEN / 2));
    assertArrayEquals(Arrays.copyOfRange(dataset, 0, TEST_FILE_LEN / 2), output2);
    stream.close();

    // Read second half of the file
    stream = getFileSystem().open(file);
    byte[] output3 = new byte[TEST_FILE_LEN / 2];
    assertEquals(TEST_FILE_LEN / 2, stream.read(TEST_FILE_LEN / 2, output3, 0, TEST_FILE_LEN / 2));
    assertArrayEquals(Arrays.copyOfRange(dataset, TEST_FILE_LEN / 2, TEST_FILE_LEN), output3);
    stream.close();

    // Read from 1/3 into the file to 2/3 into the file
    stream = getFileSystem().open(file);
    byte[] output4 = new byte[TEST_FILE_LEN / 3];
    assertEquals(TEST_FILE_LEN / 3, stream.read(TEST_FILE_LEN / 3, output4, 0, TEST_FILE_LEN / 3));
    assertArrayEquals(Arrays.copyOfRange(dataset, TEST_FILE_LEN / 3, TEST_FILE_LEN * 2 / 3), output4);
    stream.close();
  }
}
