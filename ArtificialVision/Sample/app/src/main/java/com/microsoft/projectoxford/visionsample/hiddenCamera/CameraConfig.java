/*
 * Copyright 2017 Keval Patel.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.microsoft.projectoxford.visionsample.hiddenCamera;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by Keval on 12-Nov-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public final class CameraConfig {
    private Context mContext;

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraResolution.SupportedResolution
    private int mResolution = com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraResolution.MEDIUM_RESOLUTION;

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraFacing.SupportedCameraFacing
    private int mFacing = com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraFacing.REAR_FACING_CAMERA;

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat.SupportedImageFormat
    private int mImageFormat = com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat.FORMAT_JPEG;

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.SupportedRotation
    private int mImageRotation = com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.ROTATION_0;

    private File mImageFile;

    public CameraConfig() {
    }

    public Builder getBuilder(Context context) {
        mContext = context;
        return new Builder();
    }

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraResolution.SupportedResolution
    int getResolution() {
        return mResolution;
    }

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraFacing.SupportedCameraFacing
    int getFacing() {
        return mFacing;
    }

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat.SupportedImageFormat
    int getImageFormat() {
        return mImageFormat;
    }

    File getImageFile() {
        return mImageFile;
    }

    @com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.SupportedRotation
    int getmImageRotation() {
        return mImageRotation;
    }

    public class Builder {


        public Builder setCameraResolution(@com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraResolution.SupportedResolution int resolution) {

            //Validate input
            if (resolution != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraResolution.HIGH_RESOLUTION &&
                    resolution != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraResolution.MEDIUM_RESOLUTION &&
                    resolution != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraResolution.LOW_RESOLUTION) {
                throw new RuntimeException("Invalid camera resolution.");
            }

            mResolution = resolution;
            return this;
        }

        public Builder setCameraFacing(@com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraFacing.SupportedCameraFacing int cameraFacing) {
            //Validate input
            if (cameraFacing != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraFacing.REAR_FACING_CAMERA &&
                    cameraFacing != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraFacing.FRONT_FACING_CAMERA) {
                throw new RuntimeException("Invalid camera facing value.");
            }

            mFacing = cameraFacing;
            return this;
        }

        /**
         * Specify the image format for the output image. If you don't specify any output format,
         * default output format will be {@link com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat#FORMAT_JPEG}.
         *
         * @param imageFormat Any supported image format from:
         *                    <li>{@link com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat#FORMAT_JPEG}</li>
         *                    <li>{@link com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat#FORMAT_PNG}</li>
         * @return {@link Builder}
         * @see com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat
         */
        public Builder setImageFormat(@com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat.SupportedImageFormat int imageFormat) {
            //Validate input
            if (imageFormat != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat.FORMAT_JPEG &&
                    imageFormat != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat.FORMAT_PNG) {
                throw new RuntimeException("Invalid output image format.");
            }

            mImageFormat = imageFormat;
            return this;
        }

        /**
         * Specify the output image rotation. The output image will be rotated by amount of degree specified
         * before stored to the output file. By default there is no rotation applied.
         *
         * @param rotation Any supported rotation from:
         *                 <li>{@link com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation#ROTATION_0}</li>
         *                 <li>{@link com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation#ROTATION_90}</li>
         *                 <li>{@link com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation#ROTATION_180}</li>
         *                 <li>{@link com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation#ROTATION_270}</li>
         * @return {@link Builder}
         * @see com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation
         */
        public Builder setImageRotation(@com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.SupportedRotation int rotation) {
            //Validate input
            if (rotation != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.ROTATION_0
                    && rotation != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.ROTATION_90
                    && rotation != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.ROTATION_180
                    && rotation != com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraRotation.ROTATION_270) {
                throw new RuntimeException("Invalid image rotation.");
            }

            mImageRotation = rotation;
            return this;
        }

        /**
         * Set the location of the out put image. If you do not set any file for the output image, by
         * default image will be stored in the application's cache directory.
         *
         * @param imageFile {@link File} where you want to store the image.
         * @return {@link Builder}
         */
        public Builder setImageFile(File imageFile) {
            mImageFile = imageFile;
            return this;
        }

        /**
         * Build the configuration.
         *
         * @return {@link CameraConfig}
         */
        public CameraConfig build() {
            if (mImageFile == null) mImageFile = getDefaultStorageFile();
            return CameraConfig.this;
        }

        @NonNull
        private File getDefaultStorageFile() {
            return new File(HiddenCameraUtils.getCacheDir(mContext).getAbsolutePath()
                    + File.pathSeparator
                    + "IMG_" + System.currentTimeMillis()   //IMG_214515184113123.png
                    + (mImageFormat == com.microsoft.projectoxford.visionsample.hiddenCamera.config.CameraImageFormat.FORMAT_JPEG ? ".jpeg" : ".png"));
        }
    }
}
