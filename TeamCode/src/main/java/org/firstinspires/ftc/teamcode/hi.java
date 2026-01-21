package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import java.util.List;

@TeleOp(name="Limelight Distance Tracker", group="TeleOp")
public class RobotAutoDriveByTime extends LinearOpMode {

    private Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException {
        // 1. Initialize Drive Motors
        DcMotor FLMotor = hardwareMap.dcMotor.get("FLMotor");
        DcMotor BLMotor = hardwareMap.dcMotor.get("BLMotor");
        DcMotor FRMotor = hardwareMap.dcMotor.get("FRMotor");
        DcMotor BRMotor = hardwareMap.dcMotor.get("BRMotor");

        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // 2. Initialize Limelight 3A
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start(); // Start polling
        limelight.pipelineSwitch(0); // Set to AprilTag pipeline

        telemetry.addData("Status", "Ready - Check Web UI for Green Border");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            // Check if Limelight is connected and seeing a valid target
            if (result != null && result.isValid()) {
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();

                if (!fiducials.isEmpty()) {
                    // Get the first detected tag
                    LLResultTypes.FiducialResult fiducial = fiducials.get(0);

                    // GET RELATIVE DISTANCE (Map-Independent)
                    // getCameraPoseTargetSpace gives the Camera's position relative to the Tag
                    Pose3D relPose = fiducial.getCameraPoseTargetSpace();

                    if (relPose != null) {
                        double x = relPose.getPosition().x; // Side offset (m)
                        double y = relPose.getPosition().y; // Height offset (m)
                        double z = relPose.getPosition().z; // Forward distance (m)

                        // 3D Straight-line distance
                        double distance = Math.sqrt(x*x + y*y + z*z);

                        telemetry.addData("Status", "Tag ID %d Detected", fiducial.getFiducialId());
                        telemetry.addData("Distance", "%.2f meters", distance);
                        telemetry.addData("Z (Forward)", "%.2f m", z);
                        telemetry.addData("X (Side)", "%.2f m", x);
                    }
                } else {
                    telemetry.addData("Status", "No Targets in View");

                }
            } else {
                telemetry.addData("Status", "Searching for tags/Connecting...");
            }

            // 3. Simple Mecanum Drive
            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * 1.1;
            double turn = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1.0);
            FLMotor.setPower((drive + strafe + turn) / denominator);
            BLMotor.setPower((drive - strafe + turn) / denominator);
            FRMotor.setPower((drive - strafe - turn) / denominator);
            BRMotor.setPower((drive + strafe - turn) / denominator);

            telemetry.update();
        }
    }
}
