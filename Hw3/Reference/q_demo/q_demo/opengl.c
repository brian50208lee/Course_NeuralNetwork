/*
 * MATLAB Compiler: 2.1
 * Date: Thu Oct 17 12:10:44 2002
 * Arguments: "-B" "macro_default" "-O" "all" "-O" "fold_scalar_mxarrays:on"
 * "-O" "fold_non_scalar_mxarrays:on" "-O" "optimize_integer_for_loops:on" "-O"
 * "array_indexing:on" "-O" "optimize_conditionals:on" "-B" "sgl" "-m" "-W"
 * "main" "-L" "C" "-t" "-T" "link:exe" "-h" "libmmfile.mlib" "-W" "mainhg"
 * "libmwsglm.mlib" "q_demo.m" 
 */
#include "opengl.h"
#include "libmatlbm.h"

static mxChar _array1_[130] = { 'R', 'u', 'n', '-', 't', 'i', 'm', 'e', ' ',
                                'E', 'r', 'r', 'o', 'r', ':', ' ', 'F', 'i',
                                'l', 'e', ':', ' ', 'o', 'p', 'e', 'n', 'g',
                                'l', ' ', 'L', 'i', 'n', 'e', ':', ' ', '1',
                                ' ', 'C', 'o', 'l', 'u', 'm', 'n', ':', ' ',
                                '1', ' ', 'T', 'h', 'e', ' ', 'f', 'u', 'n',
                                'c', 't', 'i', 'o', 'n', ' ', '"', 'o', 'p',
                                'e', 'n', 'g', 'l', '"', ' ', 'w', 'a', 's',
                                ' ', 'c', 'a', 'l', 'l', 'e', 'd', ' ', 'w',
                                'i', 't', 'h', ' ', 'm', 'o', 'r', 'e', ' ',
                                't', 'h', 'a', 'n', ' ', 't', 'h', 'e', ' ',
                                'd', 'e', 'c', 'l', 'a', 'r', 'e', 'd', ' ',
                                'n', 'u', 'm', 'b', 'e', 'r', ' ', 'o', 'f',
                                ' ', 'o', 'u', 't', 'p', 'u', 't', 's', ' ',
                                '(', '1', ')', '.' };
static mxArray * _mxarray0_;

static mxChar _array3_[129] = { 'R', 'u', 'n', '-', 't', 'i', 'm', 'e', ' ',
                                'E', 'r', 'r', 'o', 'r', ':', ' ', 'F', 'i',
                                'l', 'e', ':', ' ', 'o', 'p', 'e', 'n', 'g',
                                'l', ' ', 'L', 'i', 'n', 'e', ':', ' ', '1',
                                ' ', 'C', 'o', 'l', 'u', 'm', 'n', ':', ' ',
                                '1', ' ', 'T', 'h', 'e', ' ', 'f', 'u', 'n',
                                'c', 't', 'i', 'o', 'n', ' ', '"', 'o', 'p',
                                'e', 'n', 'g', 'l', '"', ' ', 'w', 'a', 's',
                                ' ', 'c', 'a', 'l', 'l', 'e', 'd', ' ', 'w',
                                'i', 't', 'h', ' ', 'm', 'o', 'r', 'e', ' ',
                                't', 'h', 'a', 'n', ' ', 't', 'h', 'e', ' ',
                                'd', 'e', 'c', 'l', 'a', 'r', 'e', 'd', ' ',
                                'n', 'u', 'm', 'b', 'e', 'r', ' ', 'o', 'f',
                                ' ', 'i', 'n', 'p', 'u', 't', 's', ' ', '(',
                                '1', ')', '.' };
static mxArray * _mxarray2_;

static mxChar _array5_[10] = { 'o', 'p', 'e', 'n', 'g',
                               'l', 'm', 'o', 'd', 'e' };
static mxArray * _mxarray4_;
static mxArray * _mxarray6_;

static mxChar _array8_[11] = { 'N', 'e', 'v', 'e', 'r', 'S',
                               'e', 'l', 'e', 'c', 't' };
static mxArray * _mxarray7_;
static mxArray * _mxarray9_;

static mxChar _array11_[6] = { 'A', 'd', 'v', 'i', 's', 'e' };
static mxArray * _mxarray10_;
static mxArray * _mxarray12_;

static mxChar _array14_[10] = { 'A', 'u', 't', 'o', 'S',
                                'e', 'l', 'e', 'c', 't' };
static mxArray * _mxarray13_;

static mxChar _array16_[11] = { 'n', 'e', 'v', 'e', 'r', 's',
                                'e', 'l', 'e', 'c', 't' };
static mxArray * _mxarray15_;

static mxChar _array18_[6] = { 'a', 'd', 'v', 'i', 's', 'e' };
static mxArray * _mxarray17_;

static mxChar _array20_[10] = { 'a', 'u', 't', 'o', 's',
                                'e', 'l', 'e', 'c', 't' };
static mxArray * _mxarray19_;

static mxChar _array22_[4] = { 'i', 'n', 'f', 'o' };
static mxArray * _mxarray21_;

static mxChar _array24_[13] = { 'g', 'e', 't', 'o', 'p', 'e', 'n',
                                'g', 'l', 'i', 'n', 'f', 'o' };
static mxArray * _mxarray23_;

static mxChar _array26_[4] = { 'd', 'a', 't', 'a' };
static mxArray * _mxarray25_;

static mxChar _array28_[13] = { 'g', 'e', 't', 'o', 'p', 'e', 'n',
                                'g', 'l', 'd', 'a', 't', 'a' };
static mxArray * _mxarray27_;

static mxChar _array30_[71] = { 'O', 'p', 'e', 'n', 'G', 'L', ' ', 'a', 'u',
                                't', 'o', ' ', 'm', 'o', 'd', 'e', ' ', 's',
                                'e', 't', 't', 'i', 'n', 'g', 's', ' ', 'a',
                                'r', 'e', ' ', 'i', 'n', 'f', 'o', ',', ' ',
                                'a', 'u', 't', 'o', 's', 'e', 'l', 'e', 'c',
                                't', ',', ' ', 'a', 'd', 'v', 'i', 's', 'e',
                                ',', ' ', 'a', 'n', 'd', ' ', 'n', 'e', 'v',
                                'e', 'r', 's', 'e', 'l', 'e', 'c', 't' };
static mxArray * _mxarray29_;

void InitializeModule_opengl(void) {
    _mxarray0_ = mclInitializeString(130, _array1_);
    _mxarray2_ = mclInitializeString(129, _array3_);
    _mxarray4_ = mclInitializeString(10, _array5_);
    _mxarray6_ = mclInitializeDouble(0.0);
    _mxarray7_ = mclInitializeString(11, _array8_);
    _mxarray9_ = mclInitializeDouble(1.0);
    _mxarray10_ = mclInitializeString(6, _array11_);
    _mxarray12_ = mclInitializeDouble(2.0);
    _mxarray13_ = mclInitializeString(10, _array14_);
    _mxarray15_ = mclInitializeString(11, _array16_);
    _mxarray17_ = mclInitializeString(6, _array18_);
    _mxarray19_ = mclInitializeString(10, _array20_);
    _mxarray21_ = mclInitializeString(4, _array22_);
    _mxarray23_ = mclInitializeString(13, _array24_);
    _mxarray25_ = mclInitializeString(4, _array26_);
    _mxarray27_ = mclInitializeString(13, _array28_);
    _mxarray29_ = mclInitializeString(71, _array30_);
}

void TerminateModule_opengl(void) {
    mxDestroyArray(_mxarray29_);
    mxDestroyArray(_mxarray27_);
    mxDestroyArray(_mxarray25_);
    mxDestroyArray(_mxarray23_);
    mxDestroyArray(_mxarray21_);
    mxDestroyArray(_mxarray19_);
    mxDestroyArray(_mxarray17_);
    mxDestroyArray(_mxarray15_);
    mxDestroyArray(_mxarray13_);
    mxDestroyArray(_mxarray12_);
    mxDestroyArray(_mxarray10_);
    mxDestroyArray(_mxarray9_);
    mxDestroyArray(_mxarray7_);
    mxDestroyArray(_mxarray6_);
    mxDestroyArray(_mxarray4_);
    mxDestroyArray(_mxarray2_);
    mxDestroyArray(_mxarray0_);
}

static mxArray * Mopengl(int nargout_, mxArray * mode);

_mexLocalFunctionTable _local_function_table_opengl
  = { 0, (mexFunctionTableEntry *)NULL };

/*
 * The function "mlfNOpengl" contains the nargout interface for the "opengl"
 * M-function from file "C:\matlabR12\toolbox\matlab\graphics\opengl.m" (lines
 * 1-57). This interface is only produced if the M-function uses the special
 * variable "nargout". The nargout interface allows the number of requested
 * outputs to be specified via the nargout argument, as opposed to the normal
 * interface which dynamically calculates the number of outputs based on the
 * number of non-NULL inputs it receives. This function processes any input
 * arguments and passes them to the implementation version of the function,
 * appearing above.
 */
mxArray * mlfNOpengl(int nargout, mxArray * mode) {
    mxArray * str = mclGetUninitializedArray();
    mlfEnterNewContext(0, 1, mode);
    str = Mopengl(nargout, mode);
    mlfRestorePreviousContext(0, 1, mode);
    return mlfReturnValue(str);
}

/*
 * The function "mlfOpengl" contains the normal interface for the "opengl"
 * M-function from file "C:\matlabR12\toolbox\matlab\graphics\opengl.m" (lines
 * 1-57). This function processes any input arguments and passes them to the
 * implementation version of the function, appearing above.
 */
mxArray * mlfOpengl(mxArray * mode) {
    int nargout = 1;
    mxArray * str = mclGetUninitializedArray();
    mlfEnterNewContext(0, 1, mode);
    str = Mopengl(nargout, mode);
    mlfRestorePreviousContext(0, 1, mode);
    return mlfReturnValue(str);
}

/*
 * The function "mlfVOpengl" contains the void interface for the "opengl"
 * M-function from file "C:\matlabR12\toolbox\matlab\graphics\opengl.m" (lines
 * 1-57). The void interface is only produced if the M-function uses the
 * special variable "nargout", and has at least one output. The void interface
 * function specifies zero output arguments to the implementation version of
 * the function, and in the event that the implementation version still returns
 * an output (which, in MATLAB, would be assigned to the "ans" variable), it
 * deallocates the output. This function processes any input arguments and
 * passes them to the implementation version of the function, appearing above.
 */
void mlfVOpengl(mxArray * mode) {
    mxArray * str = NULL;
    mlfEnterNewContext(0, 1, mode);
    str = Mopengl(0, mode);
    mlfRestorePreviousContext(0, 1, mode);
    mxDestroyArray(str);
}

/*
 * The function "mlxOpengl" contains the feval interface for the "opengl"
 * M-function from file "C:\matlabR12\toolbox\matlab\graphics\opengl.m" (lines
 * 1-57). The feval function calls the implementation version of opengl through
 * this function. This function processes any input arguments and passes them
 * to the implementation version of the function, appearing above.
 */
void mlxOpengl(int nlhs, mxArray * plhs[], int nrhs, mxArray * prhs[]) {
    mxArray * mprhs[1];
    mxArray * mplhs[1];
    int i;
    if (nlhs > 1) {
        mlfError(_mxarray0_);
    }
    if (nrhs > 1) {
        mlfError(_mxarray2_);
    }
    for (i = 0; i < 1; ++i) {
        mplhs[i] = mclGetUninitializedArray();
    }
    for (i = 0; i < 1 && i < nrhs; ++i) {
        mprhs[i] = prhs[i];
    }
    for (; i < 1; ++i) {
        mprhs[i] = NULL;
    }
    mlfEnterNewContext(0, 1, mprhs[0]);
    mplhs[0] = Mopengl(nlhs, mprhs[0]);
    mlfRestorePreviousContext(0, 1, mprhs[0]);
    plhs[0] = mplhs[0];
}

/*
 * The function "Mopengl" is the implementation version of the "opengl"
 * M-function from file "C:\matlabR12\toolbox\matlab\graphics\opengl.m" (lines
 * 1-57). It contains the actual compiled code for that M-function. It is a
 * static function and must only be called from one of the interface functions,
 * appearing below.
 */
/*
 * function str = opengl(mode)
 */
static mxArray * Mopengl(int nargout_, mxArray * mode) {
    mexLocalFunctionTable save_local_function_table_ = mclSetCurrentLocalFunctionTable(
                                                         &_local_function_table_opengl);
    int nargin_ = mclNargin(1, mode, NULL);
    mxArray * str = mclGetUninitializedArray();
    mxArray * ans = mclGetUninitializedArray();
    mxArray * current_setting = mclGetUninitializedArray();
    mclCopyArray(&mode);
    /*
     * %OPENGL Change automatic selection mode of OpenGL rendering.
     * %   The auto selection mode of OpenGL is only relevant when the
     * %   RendererMode of the FIGURE is AUTO.
     * %   OPENGL AUTOSELECT allows OpenGL to be auto selected if OpenGL
     * %   is available and if there is graphics hardware on the host machine.
     * %   OPENGL NEVERSELECT disables auto selection of OpenGL.
     * %   OPENGL ADVISE prints a string to the command window if OpenGL
     * %   rendering is advised and does not select OpenGL.
     * %   OPENGL, by itself, returns the current auto selection state.
     * %   OPENGL INFO prints information with the Version and Vendor
     * %   of the OpenGL on your system.  The return argument can be used
     * %   to programmatically determine if OpenGL is available on your
     * %   system.
     * %   OPENGL DATA returns a structure containing the same data
     * %   printed when OPENGL INFO is called.
     * %
     * %   Note that the auto selection state only specifies that OpenGL
     * %   should or not be considered for rendering, it does not
     * %   explicitly set the rendering to OpenGL.  This can be done by
     * %   setting the Renderer property of FIGURE to OpenGL,
     * %   e.g. set(GCF, 'Renderer','OpenGL');    
     * 
     * %   Copyright 1984-2000 The MathWorks, Inc. 
     * %   $Revision: 1.6 $  $Date: 2000/06/01 02:54:10 $
     * 
     * if (nargin == 0)
     */
    if (nargin_ == 0) {
        /*
         * current_setting = feature('openglmode');
         */
        mlfAssign(&current_setting, mlfNFeature(1, _mxarray4_, NULL));
        /*
         * switch(current_setting),
         */
        {
            mxArray * v_ = mclInitialize(
                             mclVv(current_setting, "current_setting"));
            if (mclSwitchCompare(v_, _mxarray6_)) {
                /*
                 * case 0,
                 * str = 'NeverSelect';
                 */
                mlfAssign(&str, _mxarray7_);
            /*
             * case 1,
             */
            } else if (mclSwitchCompare(v_, _mxarray9_)) {
                /*
                 * str = 'Advise';
                 */
                mlfAssign(&str, _mxarray10_);
            /*
             * case 2
             */
            } else if (mclSwitchCompare(v_, _mxarray12_)) {
                /*
                 * str = 'AutoSelect';
                 */
                mlfAssign(&str, _mxarray13_);
            /*
             * end
             */
            }
            mxDestroyArray(v_);
        }
    /*
     * else
     */
    } else {
        /*
         * switch(lower(mode)),
         */
        mxArray * v_ = mclInitialize(mclVe(mlfLower(mclVa(mode, "mode"))));
        if (mclSwitchCompare(v_, _mxarray15_)) {
            /*
             * case 'neverselect',
             * feature('openglmode', 0);
             */
            mclAssignAns(&ans, mlfNFeature(0, _mxarray4_, _mxarray6_));
        /*
         * case 'advise',
         */
        } else if (mclSwitchCompare(v_, _mxarray17_)) {
            /*
             * feature('openglmode', 1);
             */
            mclAssignAns(&ans, mlfNFeature(0, _mxarray4_, _mxarray9_));
        /*
         * case 'autoselect',
         */
        } else if (mclSwitchCompare(v_, _mxarray19_)) {
            /*
             * feature('openglmode', 2);
             */
            mclAssignAns(&ans, mlfNFeature(0, _mxarray4_, _mxarray12_));
        /*
         * case 'info',
         */
        } else if (mclSwitchCompare(v_, _mxarray21_)) {
            /*
             * if(nargout==0),
             */
            if (nargout_ == 0) {
                /*
                 * feature('getopenglinfo');
                 */
                mclAssignAns(&ans, mlfNFeature(0, _mxarray23_, NULL));
            /*
             * else
             */
            } else {
                /*
                 * str = feature('getopenglinfo');
                 */
                mlfAssign(&str, mlfNFeature(1, _mxarray23_, NULL));
            /*
             * end
             */
            }
        /*
         * case 'data'
         */
        } else if (mclSwitchCompare(v_, _mxarray25_)) {
            /*
             * str = feature('getopengldata');
             */
            mlfAssign(&str, mlfNFeature(1, _mxarray27_, NULL));
        /*
         * otherwise,
         */
        } else {
            /*
             * error('OpenGL auto mode settings are info, autoselect, advise, and neverselect');
             */
            mlfError(_mxarray29_);
        /*
         * end
         */
        }
        mxDestroyArray(v_);
    /*
     * end
     */
    }
    mclValidateOutput(str, 1, nargout_, "str", "opengl");
    mxDestroyArray(current_setting);
    mxDestroyArray(ans);
    mxDestroyArray(mode);
    mclSetCurrentLocalFunctionTable(save_local_function_table_);
    return str;
}
