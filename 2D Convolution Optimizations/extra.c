#include <emmintrin.h>
#include <omp.h>
#include <stdio.h>
#include <string.h>
#include <x86intrin.h>
#pragma intrinsic ( _mm_hadd_ps )

int conv2D(float* in, float* out, int data_size_X, int data_size_Y,
                    float* kernel, int kernel_x, int kernel_y)
{
	omp_set_num_threads(16);  //test computers have 16 threads; maximize performance.

    // the x coordinate of the kernel's center
    int kern_cent_X = (kernel_x - 1)/2;
    // the y coordinate of the kernel's center
    int kern_cent_Y = (kernel_y - 1)/2;  
    
    //PADDING MATRIX
    int padX = (kernel_x - kern_cent_X - 1);  //padding for X
    int padY = (kernel_y - kern_cent_Y - 1);  //padding for Y
    int newX = data_size_X + 2 * padX;	   //X size of new padded matrix
    int newY = data_size_Y + 2 * padY;	   //Y size of new padded matrix
    float padded[newX * newY];
    
    //padding top-edge and bot-edge
#pragma omp parallel for firstprivate(padX, padY, newX, newY, data_size_X)
    for (int x = 0; x < newX; x++) {
    	for (int a = 0; a < padY; a++) {
			padded[x + (a*newX)] = (float) 0;
		}

		for (int b = data_size_Y + padY; b < newY; b++) {
			padded[x + (b*newX)] = (float) 0;
		}
    }
    //padding side-edges
#pragma omp parallel for firstprivate(padX, padY, newX, newY, data_size_X)
    for (int y = 0; y < newY; y++) {
    	for (int a = 0; a < padX; a++) {
			padded[a + (y*newX)] = (float) 0;
		}
		for (int b = data_size_X + padX; b < newX; b++) {
			padded[b + y*newX] = (float) 0;
		} 
    }
    //fill in the middle with original 'in' matrix.
#pragma omp parallel for firstprivate(padX, padY, newX, newY, data_size_X)
    for (int y = padY; y < newY-padY; y++) {
    	memcpy(padded+y*newX+padX, in+(y-padY)*data_size_X, sizeof(float)*data_size_X);
    }
    
#pragma omp parallel
    {
	__m128 kernel_vect, in_padded_vect, out_vect, partialsum_vect;
	//vectors should be privatized so each thread has its own variable. MUCH FASTER NOW.
#pragma omp for private(partialsum_vect, kernel_vect, in_padded_vect, out_vect) firstprivate(padX, data_size_X, padY, data_size_Y, kernel, out, kern_cent_Y, kern_cent_X, kernel_x)
	//y,j,i,x best loop ordering for compiler.
	for(int y = padY; y < (data_size_Y + padY); y++){ // the y-coordinates of non-padded in matrix
	    for (int j = -kern_cent_Y; j <= kern_cent_Y; j++) {
		for (int i = -kern_cent_X; i <= kern_cent_X; i++) {
		    for(int x = padX; x < (data_size_X/16*16 + padX); x+=16) { // the x-coordinates of non-padded in matrix, besides the tail-part

			kernel_vect = _mm_load1_ps(&kernel[(kern_cent_X-i) + (kern_cent_Y-j)*kernel_x]);
			
			//in_padded_vect loads x to x+3
			in_padded_vect = _mm_loadu_ps(padded + ((x+i) + (y+j)*newX));
			partialsum_vect = _mm_mul_ps(kernel_vect, in_padded_vect);
			out_vect = _mm_loadu_ps(out + ((x-padX)+(y-padY)*data_size_X));
			out_vect = _mm_add_ps(out_vect, partialsum_vect);
			_mm_storeu_ps(out + ((x-padX) + (y-padY)*data_size_X), out_vect);
			
			//loads x+4 to x+7
			in_padded_vect = _mm_loadu_ps(padded + ((x+4+i) + (y+j)*newX));
			partialsum_vect = _mm_mul_ps(kernel_vect, in_padded_vect);
			out_vect = _mm_loadu_ps(out + ((x+4-padX)+(y-padY)*data_size_X));
			out_vect = _mm_add_ps(out_vect, partialsum_vect);
			_mm_storeu_ps(out + ((x+4-padX) + (y-padY)*data_size_X), out_vect);
			
			//loads x+8 to x+11
			in_padded_vect = _mm_loadu_ps(padded + ((x+8+i) + (y+j)*newX));
			partialsum_vect = _mm_mul_ps(kernel_vect, in_padded_vect);
			out_vect = _mm_loadu_ps(out + ((x+8-padX)+(y-padY)*data_size_X));
			out_vect = _mm_add_ps(out_vect, partialsum_vect);
			_mm_storeu_ps(out + ((x+8-padX) + (y-padY)*data_size_X), out_vect);

			//loads x+12 to x+15
			in_padded_vect = _mm_loadu_ps(padded + ((x+12+i) + (y+j)*newX));
			partialsum_vect = _mm_mul_ps(kernel_vect, in_padded_vect);
			out_vect = _mm_loadu_ps(out + ((x+12-padX)+(y-padY)*data_size_X));
			out_vect = _mm_add_ps(out_vect, partialsum_vect);
			_mm_storeu_ps(out + ((x+12-padX) + (y-padY)*data_size_X), out_vect);
			
			//computes 16 values in the array, so increment by 16 in the loop above.
		    }
		}
	    }	
	}
    
    //Tail Case for the last elements that cannot be done in 16s.
    //Naive implementation, with easy OpenMP paralellization makes this edge case fast.
#pragma omp for firstprivate(padY, data_size_Y, padX, data_size_X, kern_cent_Y, kern_cent_X, kernel_x, kernel, out)
        for (int y = padY; y < (data_size_Y + padY); y++) {
	    for(int j = -kern_cent_Y; j <= kern_cent_Y; j++){ // kernel unflipped y coordinate
		for(int i = -kern_cent_X; i <= kern_cent_X; i++){ // kernel unflipped x coordinate
		    for (int x = (data_size_X/16*16 + padX); x < (data_size_X + padX); x++) {
			out[(x-padX)+(y-padY)*data_size_X] += kernel[(kern_cent_X-i)+(kern_cent_Y-j)*kernel_x] * padded[(x+i) + (y+j)*newX];  
		    }
		}
	    }
	}
    }

    
    return 1;
    
}
