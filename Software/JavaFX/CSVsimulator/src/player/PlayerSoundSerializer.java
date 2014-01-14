/*
 * The MIT License
 *
 * Copyright 2014 Marco Dalla Riva <marco.dallariva@outlook.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package player;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class PlayerSoundSerializer implements Serializable {

    private byte[] buf;
    private int randomInteger =0;
    private String randomString = "";

    public PlayerSoundSerializer(String path) throws FileNotFoundException, IOException {
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[1024];

        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum); //no doubt here is 0
            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            //System.out.println("read " + readNum + " bytes,");
        }

        buf = bos.toByteArray();
    }

    /**
     * @return the buf
     */
    public byte[] getBuf() {
        return buf;
    }

    /**
     * @param buf the buf to set
     */
    public void setBuf(byte[] buf) {
        this.buf = buf;
    }


}
