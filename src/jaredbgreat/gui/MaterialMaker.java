package jaredbgreat.boulders.util;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

/**
 *
 * @author JaredBlackurn
 */
public final class MaterialMaker {
    
    
    public static Material makeBasicLitMaterial(AssetManager assetManager, ColorRGBA color) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color);
        mat.setColor("Specular", color.add(ColorRGBA.Gray));
        mat.setBoolean("UseMaterialColors", true);
        mat.setFloat("Shininess", 64.0f);
        return mat;
    }
    
    
    public static Material makeBasicMaterial(AssetManager assetManager, ColorRGBA color) {        
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        return mat;
    }
    
    
    public static Material makeGuiMaterial(AssetManager assetManager, String texture) {        
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture(texture));        
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        return mat;
    }
    
    
    public static Material makeTexturedtMaterial(AssetManager assetManager, String texture) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture(texture));
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", false);
        mat.setFloat("Shininess", 64.0f);
        return mat;
    }
    
    
    public static Material makeTexturedtMaterial(AssetManager assetManager, TextureKey key) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture(key));
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", false);
        mat.setFloat("Shininess", 64.0f);
        return mat;
    }
    
    
    public static Material makeTexturedtMaterial(AssetManager assetManager, 
            String texture, String normals) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture(texture));
        mat.setTexture("NormalMap", assetManager.loadTexture(normals));
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", false);
        mat.setFloat("Shininess", 64.0f);
        return mat;
    }
    
    
    public static Material makeTiledtMaterial(AssetManager assetManager, String texture) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        Texture tex = assetManager.loadTexture(texture);
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", true);
        mat.setFloat("Shininess", 0.0f);
        return mat;
    }
    
    
    public static Material makeTiledtMaterial(AssetManager assetManager, TextureKey key) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        Texture tex = assetManager.loadTexture(key);
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", true);
        mat.setFloat("Shininess", 0.0f);
        return mat;
    }
    
    
    public static Material makeTiledtMaterial(AssetManager assetManager, 
            String texture, String normals) {        
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");
        Texture tex = assetManager.loadTexture(texture);
        Texture nml = assetManager.loadTexture(normals);
        tex.setWrap(Texture.WrapMode.Repeat);
        nml.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);
        mat.setTexture("NormalMap", nml);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setBoolean("UseMaterialColors", true);
        mat.setFloat("Shininess", 64.0f);
        return mat;
    }
    
}
