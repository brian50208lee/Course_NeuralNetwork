/*
 * MATLAB Compiler: 2.1
 * Date: Thu Oct 17 12:10:44 2002
 * Arguments: "-B" "macro_default" "-O" "all" "-O" "fold_scalar_mxarrays:on"
 * "-O" "fold_non_scalar_mxarrays:on" "-O" "optimize_integer_for_loops:on" "-O"
 * "array_indexing:on" "-O" "optimize_conditionals:on" "-B" "sgl" "-m" "-W"
 * "main" "-L" "C" "-t" "-T" "link:exe" "-h" "libmmfile.mlib" "-W" "mainhg"
 * "libmwsglm.mlib" "q_demo.m" 
 */

#ifndef MLF_V2
#define MLF_V2 1
#endif

#ifndef __opengl_h
#define __opengl_h 1

#ifdef __cplusplus
extern "C" {
#endif

#include "libmatlb.h"

extern void InitializeModule_opengl(void);
extern void TerminateModule_opengl(void);
extern _mexLocalFunctionTable _local_function_table_opengl;

extern mxArray * mlfNOpengl(int nargout, mxArray * mode);
extern mxArray * mlfOpengl(mxArray * mode);
extern void mlfVOpengl(mxArray * mode);
extern void mlxOpengl(int nlhs, mxArray * plhs[], int nrhs, mxArray * prhs[]);

#ifdef __cplusplus
}
#endif

#endif
