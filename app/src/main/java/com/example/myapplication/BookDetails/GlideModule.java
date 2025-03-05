//package com.example.myapplication.BookDetails;
//
//
//import android.content.Context;
//import android.graphics.drawable.PictureDrawable;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
//import com.bumptech.glide.module.AppGlideModule;
//import com.caverock.androidsvg.SVG;
//
//import java.io.InputStream;
//
//@com.bumptech.glide.annotation.GlideModule
//public class GlideModulee extends AppGlideModule {
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        registry.register(SVG.class, PictureDrawable.class, new SvgDrawableTranscoder())
//                .append(InputStream.class, SVG.class, new SvgDecoder());
//    }
//}
