package ga.rafaela.doo_vivero_gr_parte_3.dao;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ga.rafaela.doo_vivero_gr_parte_3.model.Planta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GsonPlantaDAO<T extends Planta> implements PlantaDAO<T> {

    private final Path nombreArchivo;
    private final Type tipo;
    private final Gson gson = new Gson();

    public GsonPlantaDAO(Path nombreArchivo, Class<T> clase){
        this.nombreArchivo = nombreArchivo;
        this.tipo = TypeToken.getParameterized(List.class, clase).getType();
    }

    @Override
    public List<T> cargarDAO() throws PersistenciaExcepcion{

        if(Files.notExists(nombreArchivo)){
            return new ArrayList<>();
        }

        try (Reader reader = Files.newBufferedReader(nombreArchivo, StandardCharsets.UTF_8)){
            List<T> listaPlantasJson = gson.fromJson(reader,tipo);

            if(listaPlantasJson.isEmpty() || listaPlantasJson == null){
                return new ArrayList<>();
            }

            if(listaPlantasJson.contains(null)){
                throw new PersistenciaExcepcion("No se pudo cargar el archivo porque contiene un elemento vacío.");
            }
            return new ArrayList<>(listaPlantasJson);

        } catch (IOException | JsonParseException e){
            throw new PersistenciaExcepcion("No se pudo cargar información del archivo.",e);
        }
    }

    @Override
    public void guardarDAO(List<T> lista) throws PersistenciaExcepcion{

        try(Writer writer = Files.newBufferedWriter(nombreArchivo, StandardCharsets.UTF_8)){
            gson.toJson(lista,writer);

        }catch (IOException | JsonParseException e){
            throw new PersistenciaExcepcion("No se pudo guardar el archivo.",e);
        }

    }
}
