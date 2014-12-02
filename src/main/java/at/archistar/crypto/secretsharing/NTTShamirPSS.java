package at.archistar.crypto.secretsharing;

import at.archistar.crypto.decode.DecoderFactory;
import at.archistar.crypto.exceptions.WeakSecurityException;
import at.archistar.crypto.math.GFFactory;
import at.archistar.crypto.math.ntt.AbstractNTT;
import at.archistar.crypto.random.RandomSource;

/**
 * @author andy
 */
public class NTTShamirPSS extends NTTSecretSharing {
    
    private final RandomSource rng;
    
    public NTTShamirPSS(int n, int k, int generator, GFFactory factory, RandomSource rng, AbstractNTT ntt, DecoderFactory decoderFactory) throws WeakSecurityException {
        
        super(n, k, generator, factory, ntt, decoderFactory);
        
        this.rng = rng;
        
        /** how much (in bytes) can we fit per share (n shares must fit into
          * a NTTBlock)
          */
        shareSize = nttBlockLength / n;
        dataPerNTT = nttBlockLength / n * 1;
    }
    
    @Override
    protected int[] encodeData(int tmp[], int[] data, int offset, int length) {
        System.arraycopy(data, offset, tmp, 0, length);

        /* (k-1) -- shamir uses 1 byte secret and (k-1) byte randomness */
        int[] random = new int[length * (k - 1)];
        rng.fillBytesAsInts(random);

        System.arraycopy(random, 0, tmp, dataPerNTT, random.length);
        
        return tmp;
    }
}